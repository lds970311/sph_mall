import time

from selenium import webdriver


def open_url(url):
    # 实例化浏览器驱动对象（创建浏览器驱动对象）
    firefox = webdriver.Firefox()  # 创建的是谷歌浏览器驱动对象   chrome后面有括号，而且第一个字母要大写
    firefox.maximize_window()
    # 打开百度网站
    firefox.get(url)
    time.sleep(3)  # 通过快捷导包的方式导入time模块， 光标要在time后面再按alt+enter
    # 退出浏览器驱动(释放系统资源)
    # firefox.quit()
