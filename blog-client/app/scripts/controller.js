'use strict';

var postsControllers = angular.module('postsControllers', []);
var usersControllers = angular.module('usersControllers', []);
postsControllers.$inject = ['$location'];

postsControllers.controller('BlogListCtrl', ['$scope', 'Post', function ($scope, Post) {
       /*$http.get('/services/post/all/0/10').success(function(data) {
           $scope.posts = data;
       });*/
       // $scope.numPerPage = 5;
        //$scope.noOfPages = Math.ceil(myData.count() / $scope.numPerPage);
        //$scope.currentPage = 1;

        $scope.posts = Post.query();
        $scope.orderProp = 'postId';
    }
]);

postsControllers.controller('BlogDetailsCtrl', ['$scope', '$routeParams', '$location', 'Post', function ($scope, $routeParams, $location, Post) {
    $scope.post = Post.get({postId: $routeParams.postId});

    $scope.delete = function(post) {
        Post.delete(post, function() {
            $location.path('#/posts');
        });
    };


}]);

postsControllers.controller('BlogFormCtrl', ['$scope', '$routeParams', 'Post', function ($scope, $routeParams, Post) {
    $scope.master = {};

    if($routeParams.postId) {
        Post.get({postId: $routeParams.postId}, function(post) {
            $scope.master = post;
            $scope.reset();
        });
    }

    $scope.update = function(post) {
        Post.create(post, function(post) {
            $scope.master = post;
        });
    };

    $scope.reset = function() {
        $scope.post = angular.copy($scope.master);
    };


}]);



usersControllers.controller('UserFormCtrl', ['$scope', '$rootScope','$location','Security', function ($scope, $rootScope, $location, Security) {
   
    $scope.log = function(security) {
        Security.auth({}, 'client_id=cyrril&client_secret=secret&grant_type=password&username=' + security.username + '&password=' + security.password, function(security) {
            $rootScope.token = security.value;
            $location.path('#/posts');
        }, 
        function(failure) {
            // lock useraccount after sometime 
            alert(failure);

        });
    };


}]);
