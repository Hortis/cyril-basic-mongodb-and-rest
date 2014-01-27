'use strict';
var postServices = angular.module('postServices', ['ngResource']);
var secuServices = angular.module('secuServices', ['ngResource']);



postServices.factory('Post', [ '$resource',
    function ($resource) {
        return $resource('/services/post/:postId', {}, {
            query: {method: 'GET', params: {postId: ''}, isArray: true},
            post: {method: 'POST'},
            create: {method: 'PUT'},
            delete: {method: 'DELETE', params: {postId: '@postId'}}
        });
    }
]);


secuServices.factory('Security', [ '$resource',
    function ($resource) {
        return $resource('services/oauth/token', {}, {
            auth: {method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}},
        });
    }
]);
