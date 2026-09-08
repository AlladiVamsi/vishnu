-- V2__seed_data.sql
-- Development & Demo Seed Data for Plant Care Service Platform

-- 1. Seed Sample Categories
INSERT INTO plant_categories (id, name, description, active) VALUES
(gen_random_uuid(), 'Indoor Plants', 'Plants suited for indoor office desk and floor placements', true),
(gen_random_uuid(), 'Outdoor Plants', 'Sun-loving plants for garden, balcony, and outdoor corporate terraces', true),
(gen_random_uuid(), 'Office Plants', 'Low maintenance plants ideal for corporate offices and conference rooms', true),
(gen_random_uuid(), 'Air Purifying Plants', 'Plants scientifically proven to remove airborne toxins and VOCs', true),
(gen_random_uuid(), 'Flowering Plants', 'Beautiful colorful blooming plants for reception areas', true),
(gen_random_uuid(), 'Low Maintenance Plants', 'Hardy plants needing minimal watering and indirect sunlight', true),
(gen_random_uuid(), 'Decorative Plants', 'Architectural foliage plants for modern interior design', true),
(gen_random_uuid(), 'Premium Plants', 'Exotic statement plants for executive suites', true)
ON CONFLICT (name) DO NOTHING;

-- 2. Seed Maintenance Plans
INSERT INTO maintenance_plans (id, name, description, frequency, price, watering_included, cleaning_included, health_monitoring_included, fertilizer_included, replacement_included, emergency_support_included, consultation_included, active) VALUES
(gen_random_uuid(), 'BASIC CARE', 'Weekly watering and leaf dust cleaning service', 'WEEKLY', 499.00, true, true, true, false, false, false, false, true),
(gen_random_uuid(), 'STANDARD CARE', 'Bi-weekly watering, leaf shining, organic fertilization, and pest checks', 'WEEKLY', 999.00, true, true, true, true, true, false, true, true),
(gen_random_uuid(), 'PREMIUM CARE', 'Complete corporate plant care including guaranteed plant replacement and 24/7 emergency support', 'TWICE_MONTHLY', 1999.00, true, true, true, true, true, true, true, true)
ON CONFLICT (name) DO NOTHING;
