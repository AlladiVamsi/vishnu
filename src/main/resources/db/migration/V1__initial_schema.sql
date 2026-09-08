-- V1__initial_schema.sql
-- Plant Care Service Platform Initial Migration

-- 1. Users Table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(30) NOT NULL DEFAULT 'CUSTOMER',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    company_id UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Companies Table
CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_name VARCHAR(200) NOT NULL,
    registration_number VARCHAR(100) UNIQUE,
    contact_person VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(30),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add Foreign Key constraint on users.company_id
ALTER TABLE users ADD CONSTRAINT fk_users_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL;

-- 3. Company Locations Table
CREATE TABLE company_locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    location_name VARCHAR(150) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    location_type VARCHAR(50) NOT NULL DEFAULT 'OFFICE',
    indoor_outdoor VARCHAR(30) NOT NULL DEFAULT 'INDOOR',
    approximate_area DECIMAL(10, 2),
    notes TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Plant Categories Table
CREATE TABLE plant_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Plants Table
CREATE TABLE plants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id UUID NOT NULL REFERENCES plant_categories(id) ON DELETE RESTRICT,
    name VARCHAR(150) NOT NULL,
    scientific_name VARCHAR(150),
    description TEXT,
    purchase_price DECIMAL(10, 2) NOT NULL CHECK (purchase_price >= 0),
    rental_price DECIMAL(10, 2) NOT NULL CHECK (rental_price >= 0),
    installation_charge DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (installation_charge >= 0),
    size VARCHAR(50),
    height VARCHAR(50),
    indoor_outdoor VARCHAR(30) NOT NULL DEFAULT 'INDOOR',
    sunlight_requirement VARCHAR(50),
    water_requirement VARCHAR(50),
    temperature_requirement VARCHAR(50),
    humidity_requirement VARCHAR(50),
    maintenance_level VARCHAR(50),
    maintenance_frequency VARCHAR(50),
    suitable_locations TEXT,
    care_instructions TEXT,
    sale_available BOOLEAN NOT NULL DEFAULT TRUE,
    rental_available BOOLEAN NOT NULL DEFAULT TRUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 6. Plant Images Table
CREATE TABLE plant_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plant_id UUID NOT NULL REFERENCES plants(id) ON DELETE CASCADE,
    storage_key VARCHAR(255) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT,
    primary_image BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 7. Inventories Table
CREATE TABLE inventories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plant_id UUID NOT NULL UNIQUE REFERENCES plants(id) ON DELETE CASCADE,
    total_quantity INT NOT NULL DEFAULT 0 CHECK (total_quantity >= 0),
    available_quantity INT NOT NULL DEFAULT 0 CHECK (available_quantity >= 0),
    reserved_quantity INT NOT NULL DEFAULT 0 CHECK (reserved_quantity >= 0),
    installed_quantity INT NOT NULL DEFAULT 0 CHECK (installed_quantity >= 0),
    rental_quantity INT NOT NULL DEFAULT 0 CHECK (rental_quantity >= 0),
    damaged_quantity INT NOT NULL DEFAULT 0 CHECK (damaged_quantity >= 0),
    version BIGINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. Carts Table
CREATE TABLE carts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    company_id UUID REFERENCES companies(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 9. Maintenance Plans Table
CREATE TABLE maintenance_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    frequency VARCHAR(50) NOT NULL DEFAULT 'WEEKLY',
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    watering_included BOOLEAN NOT NULL DEFAULT TRUE,
    cleaning_included BOOLEAN NOT NULL DEFAULT TRUE,
    health_monitoring_included BOOLEAN NOT NULL DEFAULT TRUE,
    fertilizer_included BOOLEAN NOT NULL DEFAULT TRUE,
    replacement_included BOOLEAN NOT NULL DEFAULT FALSE,
    emergency_support_included BOOLEAN NOT NULL DEFAULT FALSE,
    consultation_included BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 10. Cart Items Table
CREATE TABLE cart_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_id UUID NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
    plant_id UUID NOT NULL REFERENCES plants(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    order_type VARCHAR(30) NOT NULL DEFAULT 'PURCHASE', -- PURCHASE, RENTAL
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    installation_charge DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    maintenance_plan_id UUID REFERENCES maintenance_plans(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 11. Bookings Table
CREATE TABLE bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE RESTRICT,
    location_id UUID NOT NULL REFERENCES company_locations(id) ON DELETE RESTRICT,
    booking_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    preferred_installation_date TIMESTAMP WITHOUT TIME ZONE,
    booking_type VARCHAR(30) NOT NULL DEFAULT 'PURCHASE', -- PURCHASE, RENTAL, MIXED
    site_inspection_required BOOLEAN NOT NULL DEFAULT FALSE,
    maintenance_plan_id UUID REFERENCES maintenance_plans(id) ON DELETE SET NULL,
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    installation_charges DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    maintenance_charges DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 12. Booking Items Table
CREATE TABLE booking_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    plant_id UUID NOT NULL REFERENCES plants(id) ON DELETE RESTRICT,
    quantity INT NOT NULL CHECK (quantity > 0),
    order_type VARCHAR(30) NOT NULL DEFAULT 'PURCHASE',
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price DECIMAL(10, 2) NOT NULL CHECK (total_price >= 0)
);

-- 13. Site Inspections Table
CREATE TABLE site_inspections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID NOT NULL UNIQUE REFERENCES bookings(id) ON DELETE CASCADE,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    location_id UUID NOT NULL REFERENCES company_locations(id) ON DELETE CASCADE,
    assigned_expert_id UUID REFERENCES users(id) ON DELETE SET NULL,
    requested_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    scheduled_date TIMESTAMP WITHOUT TIME ZONE,
    available_space DECIMAL(10, 2),
    sunlight_level VARCHAR(50),
    temperature VARCHAR(50),
    humidity VARCHAR(50),
    indoor_outdoor VARCHAR(30),
    recommendations TEXT,
    notes TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 14. Worker Profiles Table
CREATE TABLE worker_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(150) NOT NULL,
    experience VARCHAR(100),
    specialization VARCHAR(200),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    availability_status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 15. Worker Assignments Table
CREATE TABLE worker_assignments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    worker_id UUID NOT NULL REFERENCES worker_profiles(id) ON DELETE RESTRICT,
    booking_id UUID REFERENCES bookings(id) ON DELETE SET NULL,
    service_request_id UUID,
    assigned_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by UUID REFERENCES users(id) ON DELETE SET NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ASSIGNED',
    notes TEXT
);

-- 16. Subscriptions Table
CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE RESTRICT,
    booking_id UUID NOT NULL UNIQUE REFERENCES bookings(id) ON DELETE RESTRICT,
    maintenance_plan_id UUID NOT NULL REFERENCES maintenance_plans(id) ON DELETE RESTRICT,
    start_date DATE NOT NULL,
    end_date DATE,
    next_service_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    auto_renew BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 17. Service Visits Table
CREATE TABLE service_visits (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subscription_id UUID NOT NULL REFERENCES subscriptions(id) ON DELETE CASCADE,
    booking_id UUID REFERENCES bookings(id) ON DELETE SET NULL,
    worker_id UUID REFERENCES worker_profiles(id) ON DELETE SET NULL,
    scheduled_date DATE NOT NULL,
    arrival_time TIMESTAMP WITHOUT TIME ZONE,
    start_time TIMESTAMP WITHOUT TIME ZONE,
    end_time TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 18. Service Activities Table
CREATE TABLE service_activities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    service_visit_id UUID NOT NULL UNIQUE REFERENCES service_visits(id) ON DELETE CASCADE,
    watering_done BOOLEAN NOT NULL DEFAULT FALSE,
    fertilizer_applied BOOLEAN NOT NULL DEFAULT FALSE,
    cleaning_done BOOLEAN NOT NULL DEFAULT FALSE,
    damaged_leaves_removed INT DEFAULT 0,
    pest_detected BOOLEAN NOT NULL DEFAULT FALSE,
    disease_detected BOOLEAN NOT NULL DEFAULT FALSE,
    plant_replacement_needed BOOLEAN NOT NULL DEFAULT FALSE,
    overall_health_status VARCHAR(30) NOT NULL DEFAULT 'HEALTHY',
    notes TEXT
);

-- 19. Plant Instances Table
CREATE TABLE plant_instances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plant_id UUID NOT NULL REFERENCES plants(id) ON DELETE RESTRICT,
    booking_id UUID NOT NULL REFERENCES bookings(id) ON DELETE RESTRICT,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE RESTRICT,
    location_id UUID NOT NULL REFERENCES company_locations(id) ON DELETE RESTRICT,
    unique_plant_code VARCHAR(100) NOT NULL UNIQUE,
    qr_code VARCHAR(255) NOT NULL UNIQUE,
    installation_date DATE NOT NULL,
    current_health_status VARCHAR(30) NOT NULL DEFAULT 'HEALTHY',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 20. Plant Health Records Table
CREATE TABLE plant_health_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plant_instance_id UUID NOT NULL REFERENCES plant_instances(id) ON DELETE RESTRICT,
    service_visit_id UUID REFERENCES service_visits(id) ON DELETE SET NULL,
    worker_id UUID REFERENCES worker_profiles(id) ON DELETE SET NULL,
    health_status VARCHAR(30) NOT NULL DEFAULT 'HEALTHY',
    disease VARCHAR(200),
    pest VARCHAR(200),
    notes TEXT,
    photo_reference VARCHAR(500),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 21. Watering Records Table
CREATE TABLE watering_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plant_instance_id UUID NOT NULL REFERENCES plant_instances(id) ON DELETE RESTRICT,
    service_visit_id UUID REFERENCES service_visits(id) ON DELETE SET NULL,
    worker_id UUID REFERENCES worker_profiles(id) ON DELETE SET NULL,
    watering_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount_ml INT NOT NULL CHECK (amount_ml > 0),
    notes TEXT
);

-- 22. Fertilizer Records Table
CREATE TABLE fertilizer_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plant_instance_id UUID NOT NULL REFERENCES plant_instances(id) ON DELETE RESTRICT,
    service_visit_id UUID REFERENCES service_visits(id) ON DELETE SET NULL,
    worker_id UUID REFERENCES worker_profiles(id) ON DELETE SET NULL,
    fertilizer_type VARCHAR(150) NOT NULL,
    quantity_grams DECIMAL(10, 2) NOT NULL CHECK (quantity_grams > 0),
    application_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

-- 23. Plant Replacements Table
CREATE TABLE plant_replacements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    old_plant_instance_id UUID NOT NULL REFERENCES plant_instances(id) ON DELETE RESTRICT,
    new_plant_instance_id UUID REFERENCES plant_instances(id) ON DELETE SET NULL,
    service_visit_id UUID REFERENCES service_visits(id) ON DELETE SET NULL,
    reason TEXT NOT NULL,
    replacement_date DATE NOT NULL,
    replacement_cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    included_in_plan BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT
);

-- 24. Emergency Requests Table
CREATE TABLE emergency_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    location_id UUID NOT NULL REFERENCES company_locations(id) ON DELETE CASCADE,
    plant_instance_id UUID REFERENCES plant_instances(id) ON DELETE SET NULL,
    customer_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    description TEXT NOT NULL,
    priority VARCHAR(30) NOT NULL DEFAULT 'MEDIUM',
    photo_reference VARCHAR(500),
    preferred_visit_date DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    assigned_worker_id UUID REFERENCES worker_profiles(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 25. Complaints Table
CREATE TABLE complaints (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE RESTRICT,
    booking_id UUID REFERENCES bookings(id) ON DELETE SET NULL,
    service_visit_id UUID REFERENCES service_visits(id) ON DELETE SET NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    priority VARCHAR(30) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    assigned_to_id UUID REFERENCES users(id) ON DELETE SET NULL,
    resolution TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 26. Payments Table
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID NOT NULL REFERENCES bookings(id) ON DELETE RESTRICT,
    invoice_id UUID,
    transaction_id VARCHAR(150) NOT NULL UNIQUE,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'INITIATED',
    payment_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gateway_reference VARCHAR(255)
);

-- 27. Invoices Table
CREATE TABLE invoices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_number VARCHAR(100) NOT NULL UNIQUE,
    booking_id UUID NOT NULL UNIQUE REFERENCES bookings(id) ON DELETE RESTRICT,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE RESTRICT,
    subtotal DECIMAL(10, 2) NOT NULL,
    installation_charges DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    maintenance_charges DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(30) NOT NULL DEFAULT 'UNPAID',
    invoice_date DATE NOT NULL DEFAULT CURRENT_DATE
);

-- 28. Notifications Table
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    reference_type VARCHAR(50),
    reference_id VARCHAR(100),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 29. Audit Logs Table
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100),
    old_value TEXT,
    new_value TEXT,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45)
);

-- INDEXES FOR FREQUENTLY QUERIED FIELDS
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_companies_reg ON companies(registration_number);
CREATE INDEX idx_plants_name ON plants(name);
CREATE INDEX idx_plants_cat ON plants(category_id);
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_company ON bookings(company_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_service_visits_worker ON service_visits(worker_id);
CREATE INDEX idx_service_visits_date ON service_visits(scheduled_date);
CREATE INDEX idx_plant_instances_code ON plant_instances(unique_plant_code);
CREATE INDEX idx_plant_instances_qr ON plant_instances(qr_code);
CREATE INDEX idx_plant_health_instance ON plant_health_records(plant_instance_id);
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_payments_tx ON payments(transaction_id);
