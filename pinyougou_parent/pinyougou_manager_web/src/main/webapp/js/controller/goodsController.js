 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,itemCatService,goodsService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//查询实体
	$scope.findOne = function (id) {
		//var id=$location.search().id;
		var id=$location.search()["id"];

		if (id==null){
			return;
		}

		goodsService.findOne(id).success(
			function (response) {
				$scope.entity = response;
				//商品介绍
				editor.html($scope.entity.goodsDesc.introduction);
				//图片列表
				$scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
				//扩展属性列表
				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
				//规格
				$scope.entity.goodsDesc.specificationItems=JSON.parse( $scope.entity.goodsDesc.specificationItems);

				//sku列表转换,是一个集合,需要循环遍历然后转json
				for (var i = 0; i <$scope.entity.itemList.length ; i++) {

					$scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);

				}


			}
		);
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}


	//定义商品审核状态数组
	$scope.status=["未审核","审核通过","审核未通过","关闭"];//商品状态


	//定义商品分类列表
	$scope.itemCatList=[];//商品分类列表

	//加载商品分类列表
	$scope.findItemCatList=function () {
		itemCatService.findAll().success(
			function (response) {
				for (var i = 0; i <response.length ; i++) {
					//将分类列表的名称放在数组中对应id的位置上
					$scope.itemCatList[response[i].id]=response[i].name;
				}

			});
	}


	//读取一级分类
	$scope.selectItemCat1List = function () {
		itemCatService.findByParentId(0).success(
			function (response) {
				$scope.itemCat1List = response;
			}
		);
	};

	//读取二级分类
	$scope.$watch("entity.goods.category1Id", function (newValue, oldValue) {
		//根据二级显示的值,查询二级分类
		itemCatService.findByParentId(newValue).success(
			function (response) {
				$scope.itemCat2List = response;
			}
		);

	});

	//读取三级分类
	$scope.$watch("entity.goods.category2Id", function (newValue, oldValue) {
		//根据二级显示的值,查询二级分类
		itemCatService.findByParentId(newValue).success(
			function (response) {
				$scope.itemCat3List = response;
			}
		);

	});

	//三级分类后读取模板ID
	//读取三级分类
	$scope.$watch("entity.goods.category3Id", function (newValue, oldValue) {
		//根据二级显示的值,查询二级分类
		itemCatService.findOne(newValue).success(
			function (response) {
				$scope.entity.goods.typeTemplateId = response.typeId;//更新模板ID
			}
		);

	});


	//模板ID选择后,更新品牌列表
	$scope.$watch("entity.goods.typeTemplateId", function (newValue, oldValue) {
		typeTemplateService.findOne(newValue).success(
			function (response) {
				//获取类型模板
				$scope.typeTemplate = response;

				//获取品牌列表
				$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

				//更新扩展属性,如果没有ID，则加载模板中的扩展数据,避免修改时的数据被覆盖下面代码覆盖
				if ($location.search().id==null){
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
				}

			}
		);


		//获取下拉列表选项
		typeTemplateService.findSpecList(newValue).success(
			function (response) {
				$scope.specList = response;
			}
		);

	});


	//更新updateSpecAttribute
	$scope.updateSpecAttribute=function ($event,name,value) {
		var object=$scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);

		if (object!=null){
			if ($event.target.checked){
				object.attributeValue.push(value);
			}else{
				//取消勾选
				object.attributeValue.splice(object.attributeValue.indexOf(value),1);//移除选项

				//如果选项都取消了,将此条信息移除;

				if(object.attributeValue.length==0){
					$scope.entity.goodsDesc.specificationItems.splice(
						$scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}


			}
		}else{

			$scope.entity.goodsDesc.specificationItems.push(
				{"attributeName":name,"attributeValue":[value]});



		}


	}


	//创建sku列表
	$scope.createItemList=function () {
		//定义初始化数据结构
		$scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'}];

		var items=$scope.entity.goodsDesc.specificationItems;

		for (var i = 0; i <items.length ; i++) {
			$scope.entity.itemList=addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);

		}

	}

	//添加列值,深克隆的方法,columnValues是值的集合
	addColumn=function (list,columnName,columnValues) {

		//创建新的集合
		var newList=[];

		for (var i = 0; i <list.length ; i++) {
			var oldRow=list[i];

			//循环遍历,准备进行深克隆
			for (var j = 0; j <columnValues.length ; j++) {
				var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
				newRow.spec[columnName]=columnValues[j];
				newList.push(newRow);

			}
		}

		return newList;

	}


	//定义商品审核状态数组
	$scope.status=["未审核","审核通过","审核未通过","关闭"];//商品状态


	//定义商品分类列表
	$scope.itemCatList=[];//商品分类列表

	//加载商品分类列表
	$scope.findItemCatList=function () {
		itemCatService.findAll().success(
			function (response) {
				for (var i = 0; i <response.length ; i++) {
					//将分类列表的名称放在数组中对应id的位置上
					$scope.itemCatList[response[i].id]=response[i].name;
				}

			});
	}

	//根据规格名称和选项名称返回是否被勾选
	$scope.checkAttributeValue=function (specName,optionName) {

		var items = $scope.entity.goodsDesc.specificationItems;
		var object = $scope.searchObjectByKey(items,'attributeName',specName);
		if (object==null){
			return false;
		}

		return object.attributeValue.indexOf(optionName)>=0;//如果能够查到规格选项
	}


	//更改状态
	$scope.updateStatus=function (status) {
		goodsService.updateStatus($scope.selectIds,status).success(

			function (response) {
				if (response.success){//成功
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];//清空集合
				}else{
					alert(response.message);
				}
			}
		);

	}




    
});	
