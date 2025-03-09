### **Query Parameters in RESTful Web Services (With Code Example)**
Using **query parameters** for **filtering, sorting, and pagination** is a best practice in RESTful web services. Below is an example using **Spring Boot** to implement query parameters effectively.

---

## **1. Example Use Case: Fetching Products with Filters**
Let's say we have a **Product API** that allows users to:  
✅ **Filter** products by category  
✅ **Sort** products by price (ascending/descending)  
✅ **Paginate** results (page number & size)

---

## **2. Spring Boot Implementation**

### **Step 1: Define the Product Entity**
```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private Double price;

    // Constructors, Getters, and Setters
}
```

---

### **Step 2: Create the Repository**
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category, Pageable pageable);
}
```
- We use **Spring Data JPA** to fetch filtered and paginated results.

---

### **Step 3: Implement Query Parameters in Controller**
```java
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
        @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id,asc") String[] sort) {

        List<Sort.Order> orders = new ArrayList<>();
        if (sort.length > 0) {
            for (String sortOrder : sort) {
                String[] sortParams = sortOrder.split(",");
                orders.add(new Sort.Order(Sort.Direction.fromString(sortParams[1]), sortParams[0]));
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Product> productPage;

        if (category != null) {
            productPage = productRepository.findByCategory(category, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return ResponseEntity.ok(productPage.getContent());
    }
}
```
---

### **Step 4: Test API Endpoints**
#### ✅ **Fetching all products (default pagination & sorting)**
```
GET /products
```
#### ✅ **Filtering products by category**
```
GET /products?category=electronics
```
#### ✅ **Paginating results (page 2, 10 products per page)**
```
GET /products?page=1&size=10
```
#### ✅ **Sorting products by price descending**
```
GET /products?sort=price,desc
```
---

### **5. Example Response (Paginated & Filtered)**
```json
[
    {
        "id": 1,
        "name": "Smartphone",
        "category": "electronics",
        "price": 699.99
    },
    {
        "id": 2,
        "name": "Laptop",
        "category": "electronics",
        "price": 1299.99
    }
]
```

---

## **6. Why Use Query Parameters?**
✅ **Efficient Filtering:** No need to create multiple endpoints for different filters.  
✅ **Better Performance:** Supports pagination to avoid fetching all data at once.  
✅ **Flexible Sorting:** Users can control sorting dynamically via query params.
