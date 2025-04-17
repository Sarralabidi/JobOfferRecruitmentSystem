import { Component, OnInit } from '@angular/core';
import { EventCategory, EventCategoryService } from '../../services/event-category.service';

@Component({
  selector: 'app-event-category',
  templateUrl: './event-category.component.html',
  styleUrls: ['./event-category.component.css']
})
export class EventCategoryComponent implements OnInit {
  categories: EventCategory[] = [];
  newCategory: EventCategory = { name: '' };
  selectedCategory: EventCategory | null = null;

  constructor(private categoryService: EventCategoryService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getEventCategories().subscribe(categories => {
      this.categories = categories;
    });
  }

  addCategory(): void {
    if (!this.newCategory.name.trim()) return;

    this.categoryService.createEventCategory(this.newCategory).subscribe(() => {
      this.loadCategories();
      this.newCategory = { name: '' };
    });
  }

  editCategory(category: EventCategory): void {
    this.selectedCategory = { ...category }; // copie de l'objet
  }

  updateCategory(): void {
    if (this.selectedCategory && this.selectedCategory.id) {
      this.categoryService.updateEventCategory(this.selectedCategory.id, this.selectedCategory).subscribe(() => {
        this.loadCategories();
        this.cancelEdit();
      });
    }
  }

  deleteCategory(id: number): void {
    this.categoryService.deleteEventCategory(id).subscribe(() => {
      this.loadCategories();
    });
  }

  cancelEdit(): void {
    this.selectedCategory = null;
  }
}
