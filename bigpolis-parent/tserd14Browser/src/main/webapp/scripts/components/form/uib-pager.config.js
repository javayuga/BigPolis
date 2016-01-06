'use strict';

angular.module('tserd14BrowserApp')
    .config(function (uibPagerConfig) {
        uibPagerConfig.itemsPerPage = 20;
        uibPagerConfig.previousText = '«';
        uibPagerConfig.nextText = '»';
    });
