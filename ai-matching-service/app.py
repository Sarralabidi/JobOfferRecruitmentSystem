from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from sentence_transformers import SentenceTransformer, util

app = Flask(__name__)

# 🧠 Load SBERT model
model = SentenceTransformer('all-MiniLM-L6-v2')

#sql config:
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost/PI'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

# 🗂️ Define your JobOffer model (adjust fields as per your DB)

class JobOffer(db.Model):
    __tablename__ = 'job_offer'
    id = db.Column(db.Integer, primary_key=True)
    company = db.Column(db.String(255))
    description = db.Column(db.Text)
    location = db.Column(db.String(255))
    status = db.Column(db.String(50))
    title = db.Column(db.String(255))
    keywords = db.Column(db.String(255))

@app.route('/match', methods=['POST'])
def match_cv():
    data = request.get_json()
    
    job_desc = data.get("job_description", "")
    cv_sections = data.get("cv_sections", {})

    if not job_desc or not cv_sections:
        return jsonify({"error": "Missing job description or CV sections"}), 400

    match_scores = {}
    job_embedding = model.encode(job_desc, convert_to_tensor=True)

    for section, text in cv_sections.items():
        if isinstance(text, list):
            text = " ".join(text)
        section_embedding = model.encode(text, convert_to_tensor=True)
        similarity = util.cos_sim(job_embedding, section_embedding).cpu().item() * 100
        match_scores[section] = round(similarity, 2)

    global_match_score = round(sum(match_scores.values()) / len(match_scores), 2)

    return jsonify({
        "match_scores": match_scores,
        "global_match_score": global_match_score
    })

# 🌟 NEW: Smart job filtering based on CV
@app.route('/recommend_jobs', methods=['POST'])
def recommend_jobs():
    data = request.get_json()
    cv_sections = data.get("cv_sections", {})

    if not cv_sections:
        return jsonify({"error": "Missing CV sections"}), 400

    full_cv_text = " ".join(cv_sections.values()) if isinstance(cv_sections, dict) else cv_sections
    cv_embedding = model.encode(full_cv_text, convert_to_tensor=True)

    job_offers = JobOffer.query.all()
    scored_jobs = []

    for job in job_offers:
        job_embedding = model.encode(job.description or "", convert_to_tensor=True)
        score = util.cos_sim(cv_embedding, job_embedding).cpu().item() * 100
        scored_jobs.append({
            "id": job.id,
            "title": job.title,
            "description": job.description,
            "match_score": round(score, 2)
        })

    # ✅ Add filtering step here (IMPORTANT!)
    filtered_jobs = [job for job in scored_jobs if job["match_score"] >= 50]  # You can tweak threshold
    filtered_jobs.sort(key=lambda x: x["match_score"], reverse=True)

    print("📢 Filtered jobs:", filtered_jobs)  # Debug log on Flask side
    return jsonify({"recommended_jobs": filtered_jobs})


if __name__ == '__main__':
    app.run(debug=True)
