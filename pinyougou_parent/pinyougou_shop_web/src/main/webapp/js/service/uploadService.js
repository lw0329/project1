//文件上传服务层
app.service("uploadService",function ($http) {
    this.uploadFile=function () {

        var formData=new FormData();
        //formData.append("file",file.file[0]);不规范写法
        formData.append("file",document.getElementById("file").files[0]);//得到id为file对象的文件[0]

        //发送请求
        return $http({
            method:"POST",
            url:"../upload.do",
            data:formData,//表单对象
            headers:{"Content-type":undefined},
            //不指定的话,默认头信息为json;anjularjs对于post和get请求默认的Content-Type header 是application/json。
            // 通过设置‘Content-Type’: undefined，这样浏览器会帮我们把Content-Type 设置为 multipart/form-data.
            transformRequest:angular.identity




        });


    }

});