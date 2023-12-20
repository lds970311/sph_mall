from flask import Blueprint

product_blue = Blueprint('spider', __name__)
from .service import open_url


# 定义蓝图路径及请求方法和请求返回逻辑
@product_blue.route('/get', methods=['get'])
def get_product():
    return 'product'


@product_blue.route('/open', methods=['get'])
def open_baidu():
    open_url("https://www.jd.com")
    return "OK"
