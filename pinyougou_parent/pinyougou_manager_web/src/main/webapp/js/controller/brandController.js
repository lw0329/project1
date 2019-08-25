app.controller("brandController",function ($scope,$http,$controller,brandService) {

    $controller("baseController",{$scope:$scope});//继承

    //查询所有
    $scope.findAll=function () {

        /*
        * 目标：/brand/findAll.do
        * 当前：/admin/brand.html
        * */
        brandService.findAll().success(

            function (response) {
                $scope.list=response;
            }

        );
    }




    //分页查询方法
    $scope.findPage=function (page,size) {

        brandService.findPage(page,size).success(
            //brand/findPage.do?page=1&size=10
            function (response) {
                $scope.list=response.rows;//显示当前页的数据
                $scope.paginationConf.totalItems=response.total//更新总记录数
            }

        );



    }


    //新增
    $scope.save=function () {
        var object=null;

        if ($scope.entity.id!=null){
            object=brandService.update($scope.entity);
        }else{
            object=brandService.add($scope.entity);
        }

        object.success(

            function (response) {
                if (response.success){
                    $scope.reloadList();//刷新列表
                }else{
                    alert(response.message);
                }

            }
        );

    }


    //根据id查询实体
    $scope.findOne=function (id) {

        brandService.findOne(id).success(

            function (response) {
                $scope.entity=response;

            }
        );

    }



    //批量删除
    $scope.dele=function () {

        //获取选中的复选框
        brandService.dele($scope.selectIds).success(
            function (response) {
                if (response.success){
                    //刷新列表
                    $scope.reloadList();
                }else{
                    alert(response.message)
                }

                $scope.selectIds=[];//清空id集合
            }
        );

    }


    //定义搜索对象
    $scope.searchEntity={};

    //条件查询
    $scope.search=function (page,size) {

        brandService.search(page,size,$scope.searchEntity).success(
            //brand/findPage.do?page=1&size=10
            function (response) {
                $scope.list=response.rows;//显示当前页的数据
                $scope.paginationConf.totalItems=response.total//更新总记录数
            }

        );


    }






});