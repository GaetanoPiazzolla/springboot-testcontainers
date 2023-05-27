from flask import Flask
from flask import jsonify

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello, Docker!'


@app.route('/customers')
def customer():
    list = [
        {'name': 'Python Tano', 'id': 201},
        {'name': 'Python Polp', 'id': 202}
    ]
    return jsonify(list)
