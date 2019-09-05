//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            $scope.entity.parentId = $scope.parentId;//保存时,添加父类id
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    //$scope.reloadList();//重新加载
                    $scope.findByParentId($scope.parentId);
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {

        var myConfirm = confirm("确认要删除吗?");

        if (myConfirm){
            //获取选中的复选框
            itemCatService.dele($scope.selectIds).success(
                function (response) {
                    alert(response.message);
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                        $scope.selectIds = [];
                    }
                }
            );
        }

    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        itemCatService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //根据上级Id查询下级列表
    $scope.findByParentId = function (parentId) {
        $scope.parentId = parentId; //查询时,记录parentId;

        itemCatService.findByParentId(parentId).success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //设置默认级别为1
    $scope.grade = 1;

    //设置级别
    $scope.setGrade = function (grade) {
        $scope.grade = grade;
    }

    //读取列表
    $scope.selectList = function (p_entity) {

        if ($scope.grade == 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }

        if ($scope.grade == 2) {
            $scope.entity_1 = p_entity;
            $scope.entity_2 = null;
        }

        if ($scope.grade == 3) {
            $scope.entity_2 = p_entity;
        }

        $scope.findByParentId(p_entity.id);

    }


    //定义变量,上级id,在查询时,记录上级id
    $scope.parentId = 0;


    //定义初始值
    $scope.typeTempList = {data: []};
    //定义下拉列表
    $scope.findSelectTypeTempList = function () {
        typeTemplateService.selectTypeTempList().success(
            function (response) {

                $scope.typeTempList = {data: response};
            }
        );

    }


});
