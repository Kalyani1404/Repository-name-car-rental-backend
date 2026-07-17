# RentRide Backend Feature Foundation

This build preserves the existing authentication, cars, bookings, payments and users APIs and adds production-ready foundations for the three roles.

## USER APIs
- POST /api/auth/register
- POST /api/auth/login
- GET /api/cars
- POST /api/bookings
- GET /api/bookings
- POST /api/payments
- GET /api/payments
- GET /api/users/me
- CRUD /api/addresses
- CRUD /api/emergency-contacts
- GET/PATCH /api/notifications
- POST /api/reviews
- GET /api/reviews/car/{carId}

## DRIVER APIs
- POST /api/auth/register-driver
- GET /api/driver/profile
- POST /api/driver/profile
- PATCH /api/driver/availability?online=true
- PATCH /api/driver/location?latitude=...&longitude=...

## ADMIN APIs
- GET /api/admin/dashboard
- GET /api/admin/drivers
- PATCH /api/admin/drivers/{id}/verification?status=APPROVED
- PATCH /api/admin/users/{id}/status?status=ACTIVE
- CRUD /api/cars
- CRUD /api/coupons
- GET /api/users

## Security
- JWT stateless authentication
- Role protection for ADMIN and DRIVER routes
- Public registration/login endpoints
- Localhost wildcard CORS plus configured production frontend URL

## Database compatibility
The `email_verified` field uses a PostgreSQL default and remains Java-defaulted to false, preventing existing databases from failing when the column is added. The seeded admin is verified automatically.

## Important production integrations still requiring provider credentials
Live maps/navigation, SMS/email OTP delivery, payment gateway capture/refunds, push notifications and cloud document storage require provider accounts and secrets. The backend foundation is ready for those integrations, but credentials are intentionally not embedded.
