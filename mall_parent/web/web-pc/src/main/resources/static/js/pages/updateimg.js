window.onload = function () {
    const input = document.getElementById("upgteimg");
    const showui = document.getElementById("showui");
    const upimg = document.getElementById("image-upload");
    let result;
    let dataArr = []; // 储存所选图片的结果(文件名和base64数据)
    let fd; //FormData方式发送请求
    const showinput = document.getElementById("showinput");
    const oSubmit = document.getElementById("submit");
    let dateli, dateinput;

    function randomString(len) {
        len = len || 32;
        var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
        /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
        var maxPos = $chars.length;
        var pwd = '';
        for (i = 0; i < len; i++) {
            pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
        }
        return pwd;
    }

    if (typeof FileReader === 'undefined') {
        alert("抱歉，你的浏览器不支持 FileReader");
        input.setAttribute('disabled', 'disabled');
    } else {
        input.addEventListener('change', readFile, false);
    }

    function readFile() {
        fd = new FormData();
        const iLen = this.files.length;
        let index = 0;
        let currentReViewImgIndex = 0;
        for (let i = 0; i < iLen; i++) {
            if (!input['value'].match(/.jpg|.gif|.png|.jpeg|.bmp/i)) {　　 //判断上传文件格式
                return alert("上传的图片格式不正确，请重新选择");
            }
            const reader = new FileReader();
            reader.index = i;
            fd.append(i, this.files[i]);
            reader.readAsDataURL(this.files[i]); //转成base64
            reader.fileName = this.files[i].name;
            reader.files = this.files[i];
            reader.onload = function (e) {
                const imgMsg = {
                    name: randomString(5), //获取文件名
                    base64: this.result, //reader.readAsDataURL方法执行完后，base64数据储存在reader.result里
                };
                dataArr.push(imgMsg);
                for (let j = 0; j < dataArr.length; j++) {
                    currentReViewImgIndex = j
                }
                result = '<img id="img' + currentReViewImgIndex + randomString(1) + randomString(2) + randomString(5) + '" class="showimg" src="' + this.result + '" />';
                const li = document.createElement('li');
                li.innerHTML = result;
                showui.appendChild(li);
                index++;
                const lilength = document.querySelectorAll('ul#showui li').length;
                const tip = '已上传 ' + lilength + ' 件，还能上传' + (9 - lilength) + '张';

                const span = document.createElement('span');

                //span.html(tip);
                // span.innerHTML = tip;
                upimg.appendChild(span);
                // p1.html(text1.val());
                console.log(tip)
            }
        }

    }

    function onclickimg() {
        const dataArrlist = dataArr.length;
        const lilength = document.querySelectorAll('ul#showui li');
        for (let i = 0; i < lilength.length; i++) {
            lilength[i].getElementsByTagName('img')[0].onclick = function (num) {
                return function () {
                    if (num === 0) {
                    } else {
                        let list = 0;
                        for (var j = 0; j < dataArr.length; j++) {
                            list = j
                        }
                        const up = num - 1;
                        dataArr.splice(up, 0, dataArr[num]);
                        dataArr.splice(num + 1, 1)
                        const lists = $("ul#showui li").length;
                        for (var j = 0; j < lists; j++) {
                            const usid = $("ul#showui li")[j].getElementsByTagName('img')[3];
                            $("#" + usid.id + "").attr("src", dataArr[j].base64)
                        }
                    }
                }
            }(i)
            //删除图标
            lilength[i].getElementsByTagName('img')[1].onclick = function (num) {
                return function () {
                    if (dataArr.length === 1) {
                        dataArr = [];
                        $("ul#showui").html("")
                    } else {
                        $("ul#showui li:eq(" + num + ")").remove()
                        dataArr.splice(num, 1)
                    }

                }
            }(i)
            //右箭头图标
            lilength[i].getElementsByTagName('img')[2].onclick = function (num) {
                return function () {
                    let list = 0;
                    for (var j = 0; j < dataArr.length; j++) {
                        list = j
                    }
                    const datalist = list + 1;
                    dataArr.splice(datalist, 0, dataArr[num]);
                    dataArr.splice(num, 1)
                    const lists = $("ul#showui li").length;
                    for (var j = 0; j < lists; j++) {
                        var usid = $("ul#showui li")[j].getElementsByTagName('img')[3];
                        $("#" + usid.id + "").attr("src", dataArr[j].base64)
                    }

                }
            }(i)

        }
    }

    showui.addEventListener("click", function () {
        onclickimg();
    }, true)

    function send() {
        for (var j = 0; j < dataArr.length; j++) {
            console.log(dataArr[j].name)
        }
    }


}