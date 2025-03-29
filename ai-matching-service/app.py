from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer, util

app = Flask(__name__)

# Load Sentence-BERT model
model = SentenceTransformer('all-MiniLM-L6-v2')

@app.route('/match', methods=['POST'])
def match_cv():
    data = request.get_json()
    
    job_desc = data.get("job_description", "")
    cv_sections = data.get("cv_sections", {})

    if not job_desc or not cv_sections:
        return jsonify({"error": "Missing job description or CV sections"}), 400

    # Compute similarity scores per section
    match_scores = {}
    job_embedding = model.encode(job_desc, convert_to_tensor=True)

    for section, text in cv_sections.items():
        #  FIX: If `text` is a list, join it into a single string
        if isinstance(text, list):
            text = " ".join(text)  # Join list items into one string

        section_embedding = model.encode(text, convert_to_tensor=True)

        #  FIX: Ensure output is a scalar by using `.cpu().item()`
        similarity = util.cos_sim(job_embedding, section_embedding).cpu().item() * 100  # Convert to percentage
        
        match_scores[section] = round(similarity, 2)
        global_match_score = round(sum(match_scores.values()) / len(match_scores), 2)


    return jsonify({
    "match_scores": match_scores,
    "global_match_score": global_match_score  # Added global percentage
})

if __name__ == '__main__':
    app.run(debug=True)
