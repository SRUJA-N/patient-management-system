$json = '{"name":"Jane Doe","email":"jane.doe@test.com","phoneNumber":"5551234567","priority":2,"dateOfBirth":"1995-03-20","registerDate":"2026-02-15"}'
Invoke-WebRequest -Uri 'http://localhost:4000/patient' -Method POST -ContentType 'application/json' -Body $json -UseBasicParsing
