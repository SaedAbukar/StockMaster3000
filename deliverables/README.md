# StockMaster3000: Project Plan

## 1. Project Overview
- **Project Title:** StockMaster3000
- **Problem Summary:** Poor food management inventory can lead to food waste, overspending, and poor dietary choices. StockMaster3000 aims to offer an intuitive tool to track groceries, inventory clearness, and provide useful analysis.
- **Target Users:** Students and busy professionals, budget-conscious individuals.
- **Main Features/Components:**
    - **Inventory Tracking:** Add and manage items with expiration dates, quantities, and nutritional information.
    - **Shopping Lists:** Generate shopping lists based on inventory status.
    - **Analytics and Insights:** Visualize spending patterns and calorie intake.
    - **Alerts and Notifications:** Reminders for expiring or left-in-the-fridge-too-long items to minimize waste.

---

## 2. Project Objectives
- Develop a stable and user-friendly application with essential inventory management features. Satisfaction rate must be at least 70% in user feedback during open testing.
- Deliver the first project prototype within 8 weeks, aligned with sprint goals.
- Reduce food waste by 20% for active users after implementation.

---

## 3. Scope and Deliverables
- **In Scope:**
    - User registration and authentication.
    - Inventory management features.
    - Stock tracking with alerts and notifications.
    - Reports on spending and food usage trends.
- **Out of Scope:**
    - Advanced AI analysis and dietary recommendations.

---

## 4. Work Breakdown Structure (WBS)
1. **Requirements Gathering:**
    - Collaborate as a team to identify key features (e.g., inventory tracking, notifications).
    - Document requirements in user stories to align with Agile principles.
2. **UI Design with Vaadin:**
    - Design the application’s interface directly using Vaadin’s components.
    - Create a basic dashboard for managing inventory and user reports.
3. **Backend Development with Spring Boot:**
    - Develop APIs for inventory management, user authentication, notifications, and analytics reporting.
    - Integrate the backend with database support for inventory, user data, and analytics.
4. **Database Setup:**
    - Define schemas for users, inventory, products, manufacturers, and spending/nutrition records.
    - Configure MariaDB to handle data storage and retrieval efficiently.
5. **Integration and Testing:**
    - Conduct integration testing between Vaadin and Spring Boot.
    - Write and execute unit tests for backend functionality (e.g., inventory updates, reports).
    - Validate UI workflows using manual and automated tools.
6. **Report and Analysis:**
    - Build backend logic and frontend visuals for spending and nutrition reports.
    - Ensure data accuracy and usability through testing and iterative improvement.
7. **Final Deployment and Testing:**
    - Deploy the application on a local server or cloud service for user testing.
    - Perform user acceptance testing (UAT) and gather feedback for refinements.

---

## 5. Project Timeline
- **Week 1-2:** (13.01.25 – 26.01.25)
    - Gather and document requirements for inventory management features.
    - Design the database schema and create tables (e.g., users, inventory, notifications).
    - Develop basic Vaadin UI forms for login, registration, and dashboard.
- **Week 3-4:** (27.01.25 – 09.02.25)
    - Implement CRUD functionality for inventory items using Spring Boot APIs.
    - Connect Vaadin frontend to the Spring Boot backend for data handling.
    - Begin basic testing of database interactions and API responses.
- **Week 5-6:** (10.02.25 – 23.02.25)
    - Add features like notifications for expiring items and inventory reports.
    - Enhance UI/UX design using Vaadin's layout and styling features.
    - Perform integration testing to ensure smooth operation between frontend and backend.
- **Week 7:** (24.02.25 – 01.03.25)
    - Integrate final features, including role-based access (e.g., admin, user).
    - Conduct thorough testing of the application, covering unit, integration, and system tests.
- **Week 8:** (02.03.25 – 08.03.25)
    - Prepare the final project presentation and demonstrate the working system.
    - Write and compile documentation, including user manuals and technical guides.

---

## 6. Resource Allocation
1. **Team Members and Roles:**
    - **Scrum Master:** Rotates weekly among team members to ensure all members learn Agile leadership principles.
        - Saed - Week 1
        - Viet - Week 2
        - Pavel - Week 3
        - Ivan - Week 4
    - **Product Owner:** The teacher acts as the Product Owner, providing requirements and feedback.
    - **Development Team:**
        - **Frontend Pair:** Pavel and Ivan (Vaadin UI).
        - **Backend Pair:** Saed and Viet (Spring Boot APIs).

2. **Software, Hardware, and Tools:**
    - **Planning Tools:**
        - Trello for sprint and task management.
        - Discord for communication and daily scrums.
    - **Development Tools:**
        - GitHub for version control.
        - Spring Boot (backend) and Vaadin (frontend).
    - **Testing and CI/CD:**
        - JUnit for unit testing.
        - Jenkins for continuous integration and deployment.

3. **External Resources or Support:**
    - YouTube tutorials and school lecture materials for learning and troubleshooting.

---

## 7. Risk Management
| **Risk Description**               | **Likelihood** | **Impact** | **Mitigation Strategy**                   |
|------------------------------------|----------------|------------|-------------------------------------------|
| Delays in Backend API development  | Medium         | High       | Allow buffer time; use Agile iterations.  |
| User feedback not collected efficiently | Low         | Medium     | Conduct regular user testing sessions.    |
| Technical bugs during integration  | High           | High       | Implement thorough testing before merging.|

---

## 8. Testing and Quality Assurance
- **Types of Testing:**
    - Unit Testing: Test individual components (e.g., backend APIs for inventory management).
    - Integration Testing: Verify interaction between Vaadin frontend, Spring Boot backend, and database.
    - User Acceptance Testing (UAT): Test with classmates to ensure functionality and usability.
- **Criteria for Success:**
    - Unit Tests: Key functions pass without critical errors.
    - Integration Tests: Frontend and backend work seamlessly together.
    - UAT: Positive feedback from most testers confirming usability and basic functionality.
- **Tools and Frameworks:**
    - JUnit: For unit testing backend logic.
    - Postman: To manually test API endpoints.
    - Jenkins: Automate and run tests during integration.

---

## 9. Documentation Deliverables
- Product vision
- Evaluation plan
- User manual explaining key features
- Technical documentation for developers  
