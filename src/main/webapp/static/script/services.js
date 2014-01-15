var postServices = angular.module('postServices', ['ngResource']);

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