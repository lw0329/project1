var app=angular.module("pinyougou",[]);//定义品优购模块

//定义过滤器,一个过滤器只做一件事;
app.filter("trustHtml",["$sce",function ($sce) {
    return function (data) {//传入的数据
        return $sce.trustAsHtml(data);//返回的是过滤后的内容(信任html的转换)
    }
}]);