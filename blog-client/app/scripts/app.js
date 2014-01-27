'use strict';
var blogApp = angular.module('blogApp', ['ngRoute', 'postsControllers', 'postServices', 'usersControllers', 'secuServices']);

blogApp.factory('globalResponseInterceptor',['$q','$location','$rootScope',function($q,$location,$rootScope){
    return {
        'responseError': function(rejection) {
            // do something on error
            if (rejection.status === 401) {
                $location.path('/auth');
            }
            return $q.reject(rejection);
        },
        'request': function(config) {
            config.headers.Authorization = 'Bearer ' + $rootScope.token;
            return config || $q.when(config);
        }

    };
}]);

blogApp.config([
    '$routeProvider', function($routeProvider) {
        $routeProvider
            .when('/posts', {
                templateUrl: 'partial/post-list.html',
                controller: 'BlogListCtrl'
            })
            .when('/posts/:postId', {
                templateUrl: 'partial/post-detail.html',
                controller: 'BlogDetailsCtrl'
            })
            .when('/posts/create/:postId', {
                templateUrl: 'partial/post-form.html',
                controller: 'BlogFormCtrl'
            })
            .when('/posts/create/new/', {
                templateUrl: 'partial/post-form.html',
                controller: 'BlogFormCtrl'
            })
            .when('/auth', {
                templateUrl: 'partial/user-form.html',
                controller: 'UserFormCtrl'
            })
            .otherwise({
                'redirectTo': '/posts'
            });
    }
]);

blogApp.config([
    '$httpProvider',function($httpProvider) {
        $httpProvider.interceptors.push('globalResponseInterceptor');
    }
]);
