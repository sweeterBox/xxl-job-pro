
function Toasts() {

}

Toasts.success = function (param = {title: '', content: '', autohide: true}) {
    $(document).Toasts('create', {
        class: 'card toasts-success',
        // icon: 'fas fa-exclamation-triangle',
        delay: 3000,
        autohide: param.autohide,
        title: param && param.title || '成功',
        body: param && param.content || '操作成功'
    });
};

Toasts.info = function (param = {title: '', content: '', autohide: true}) {
    $(document).Toasts('create', {
        class: 'card toasts-info',
        // icon: 'fas fa-exclamation-triangle',
        delay: 3000,
        autohide: param.autohide,
        title: param && param.title || '提示',
        body: param && param.content || ''
    });
};

Toasts.error = function (param = {title: '', content: '', autohide: true}) {
    let content = '<span style="width: 200px">' + param && param.content || '' + '</span>';
    $(document).Toasts('create', {
        class: 'card toasts-error',
       // icon: 'fas fa-exclamation-triangle',
        delay: 3000,
        autohide: param.autohide,
        title: param && param.title || '错误',
        body: content
    });
};
Toasts.warn = function (param = {title: '', content: '', autohide: true}) {

    let content = '<span style="width: 200px">'+param && param.content || ''+'</span>';
    $(document).Toasts('create', {
        class: 'card toasts-warn',
       // icon: 'fas fa-exclamation-triangle',
        delay: 3000,
        autohide: param.autohide,
        title: param && param.title || '警告',
        body: content
    });
};

