package com.apexretail.domain;

import java.util.Objects;

/**
 * Represents a logical grouping of products in the retail system.
 *
 * <p>
 * Category is an immutable domain object used to classify products
 * for organization, reporting, and business rules. Categories are
 * identified by a unique ID and equality is based solely on the ID.
 *
 * <p>
 * Instances are validated at construction time and cannot be modified
 * after creation.
 *
 * <p>
 * Example:
 * 
 * <pre>{@code
 * Category electronics = new Category(101, "Electronics", "Electronic devices and accessories");
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public final class Category {

    /** Unique category identifier (immutable). */
    private final long id;

    /** Human-readable category name (immutable). */
    private final String name;

    /** Optional category description (immutable). */
    private final String description;

    /**
     * Creates a new Category with validated attributes.
     *
     * @param id          unique category identifier (must be ≥ 0)
     * @param name        category name (must not be null or blank)
     * @param description optional category description (may be null or empty)
     * @throws IllegalArgumentException if validation fails
     */
    public Category(long id, String name, String description) {
        validateId(id);
        validateName(name);

        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Validates category ID is non-negative.
     * 
     * @param id category ID to validate
     * @throws IllegalArgumentException if ID is negative
     */
    private void validateId(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Category id must be greater than or equal to 0.");
        }
    }

    /**
     * Validates category name is not null or blank.
     * 
     * @param name category name to validate
     * @throws IllegalArgumentException if name is null or blank
     */
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name must not be null or blank.");
        }
    }

    /**
     * Returns the unique category identifier.
     * 
     * @return category ID
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the category name.
     * 
     * @return category name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the category description.
     * 
     * @return category description (may be null)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Compares categories for equality based solely on ID.
     * 
     * @param o object to compare
     * @return true if objects are equal (same ID), false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Category))
            return false;
        Category category = (Category) o;
        return id == category.id;
    }

    /**
     * Returns hash code based on category ID.
     * 
     * @return hash code of category ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns string representation of category.
     * 
     * @return formatted string containing category attributes
     */
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}