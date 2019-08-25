app.controller("baseController",function ($scope) {

    //分页控件配置
    $scope.paginationConf={
        currentPage:1,//当前页
        totalItems:10,//总记录数
        itemsPerPage:10,//每页显示条数
        perPageOptions:[10, 20, 30, 40, 50],//分页下拉选项

        onChange:function () {
            //当页码,分页数量变更后自动触发的方法;
            //$scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
            $scope.reloadList();

        }


    }


    //定义刷新列表方法
    $scope.reloadList=function(){
        // $scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }




    $scope.selectIds=[];//选中的id集合
    //更新复选框
    $scope.updateSelection=function ($event,id) {

        if ($event.target.checked){//若是被选中,则添加到数组

            $scope.selectIds.push(id);

        }else{
            var idx=$scope.selectIds.indexOf(id);

            $scope.selectIds.splice(idx,1);
        }

    }

    //提取json字符串数据中的某个属性,返回拼接字符串,逗号分隔
    $scope.jsonToString=function (jsonString,key) {

        var json=JSON.parse(jsonString);//将json字符串转为json对象
        var value="";
        for(var i=0;i<json.length;i++){

            if(i>0){
                value+=",";
            }

            value +=json[i][key];//此处key为变量,如果key为常量,可使用json[i].key的方式
        }

        return value;

    }






});