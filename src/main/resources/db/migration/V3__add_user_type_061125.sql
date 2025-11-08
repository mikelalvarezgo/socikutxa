-- Create user type enum
CREATE TYPE user_type AS ENUM ('MEMBER', 'CONSUMER');

-- Add user_type column to user table
ALTER TABLE "user"
ADD COLUMN user_type user_type NOT NULL DEFAULT 'CONSUMER';

-- Add index for faster queries by user type
CREATE INDEX idx_user_type ON "user"(user_type);

-- Add index on email for faster authentication queries
CREATE INDEX idx_user_email ON "user"(email);
