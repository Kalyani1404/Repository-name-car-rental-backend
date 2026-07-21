# Pagination and Sorting APIs

These endpoints were added without removing or changing the existing list endpoints. This keeps the current frontend compatible.

Base URL locally (because the project uses `/api` context path):

`http://localhost:8081/api`

All endpoints below require a JWT Bearer token. Admin-only endpoints still require an ADMIN token.

## Common query parameters

- `page`: zero-based page number. Default `0`.
- `size`: records per page. Default `10`. Allowed `1` to `100`.
- `sortBy`: entity field used for sorting.
- `sortDir`: `asc` or `desc`.

## Cars

`GET /cars/page?page=0&size=5&sortBy=pricePerDay&sortDir=asc`

Allowed `sortBy`: `id`, `name`, `brand`, `model`, `year`, `pricePerDay`, `rating`, `createdAt`

Existing `GET /cars` remains unchanged.

## Bookings

`GET /bookings/page?page=0&size=10&sortBy=createdAt&sortDir=desc`

Allowed `sortBy`: `id`, `bookingReference`, `startDate`, `endDate`, `createdAt`, `grandTotal`, `status`

- USER sees only their bookings.
- ADMIN sees all bookings.
- Existing `GET /bookings` remains unchanged.

## Payments

`GET /payments/page?page=0&size=10&sortBy=amount&sortDir=desc`

Allowed `sortBy`: `id`, `transactionReference`, `amount`, `status`, `paymentMethod`, `createdAt`

- USER sees only their payments.
- ADMIN sees all payments.
- Existing `GET /payments` remains unchanged.

## Users (ADMIN only)

`GET /users/page?page=0&size=10&sortBy=fullName&sortDir=asc`

Allowed `sortBy`: `id`, `fullName`, `email`, `role`, `status`, `createdAt`

Existing `GET /users` remains unchanged.

## Drivers (ADMIN only)

`GET /admin/drivers/page?page=0&size=10&sortBy=averageRating&sortDir=desc`

Allowed `sortBy`: `id`, `verificationStatus`, `online`, `completedTrips`, `averageRating`, `updatedAt`

Existing `GET /admin/drivers` remains unchanged.

## Example paginated response

```json
{
  "success": true,
  "message": "Cars loaded with pagination and sorting",
  "data": {
    "content": [],
    "page": 0,
    "size": 5,
    "totalElements": 25,
    "totalPages": 5,
    "first": true,
    "last": false,
    "sortBy": "pricePerDay",
    "sortDir": "asc"
  }
}
```
