var blogApp = angular.module('blogApp', ['ngRoute', 'postsControllers', 'postServices'])

blogApp.config([
    '$routeProvider', function($routeProvider) {
        $routeProvider
            .when('/posts', {
                templateUrl: '/static/partial/post-list.html',
                controller: 'BlogListCtrl'
            })
            .when('/posts/:postId', {
                templateUrl: '/static/partial/post-detail.html',
                controller: 'BlogDetailsCtrl'
            })
            .when('/posts/create/:postId', {
                templateUrl: '/static/partial/post-form.html',
                controller: 'BlogFormCtrl'
            })
            .when('/posts/create/new/', {
                templateUrl: '/static/partial/post-form.html',
                controller: 'BlogFormCtrl'
            })
            .otherwise({
                'redirectTo': '/posts'
            });
    }
]);
