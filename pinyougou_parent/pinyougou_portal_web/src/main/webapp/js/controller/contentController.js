//广告控制层
app.controller("contentController",function ($scope,contentService) {

    //定义广告集合
    $scope.contentList=[];//广告集合

    $scope.findByCategoryId=function (categoryId) {

        contentService.findByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList[categoryId]=response;
            }
        );

    }
    
});