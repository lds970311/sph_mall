from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()


class User(db.Model):
    __tablename__ = 'users'
    username = db.Column(db.String(80), unique=True)
    pw_hash = db.Column(db.String(80))
    id = db.Column(db.Integer, primary_key=True)
