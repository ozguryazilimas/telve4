
function toggleActionIcons(elementId) {
    var elem = $(elementId);
    if (elem.style.display == '') {
        elem.style.display = 'none';
    } else {
        elem.style.display = '';
    }
}


function deleteConfirmation() {
    return !confirm('Kaydı silmek istediğinizden emin misiniz?');
}


function openHelp() {
    newwin = window.open(null, 'HelpWinId', 'width=850,height=600,scrollbars=yes,resizable=yes,status=no,toolbar=no,location=no,menubar=no,directories=no')
    if (window.focus) {
        newwin.focus()
    }

}


function enableRibbonBtn(ids) {
    jQuery(ids).each(function () {
        jQuery(this).enable();
    });
}

function disableRibbonBtn(ids) {
    jQuery(ids).each(function () {
        jQuery(this).disable();
    });
}

function adminLTEActivate() {
    if (typeof $.AdminLTE.controlSidebar !== "undefined") {
        $.AdminLTE.controlSidebar.activate();
        var o = $.AdminLTE.options;

        $(".navbar .menu").slimscroll({
            height: o.navbarMenuHeight,
            alwaysVisible: false,
            size: o.navbarMenuSlimscrollWidth
        }).css("width", "100%");
    }
}

function bindEnters() {
    /*
     jQuery(window).off('.telve').on('keydown.telve', function (e) {
     if ( !e.ctrlKey && e.keyCode == 13 ) {
     e.preventDefault();
     e.stopImmediatePropagation();
     return false;
     }
     });*/

    jQuery('.ui-autocomplete-input').off('.telve').on('keyup.telve', function (e) {
        if (!e.ctrlKey && e.keyCode == 13) {
            e.preventDefault();
            e.stopImmediatePropagation();
            return false;
        }
    });

    jQuery('.input-group > .ui-inputfield').off('.telve').on('keyup.telve', function (e) {
        if (!e.ctrlKey && e.keyCode == 13) {
            e.preventDefault();
            e.stopImmediatePropagation();
            return false;
        }
    });

    /*
     jQuery(window).on('keyup.telve', function (e) {
     if ( !e.ctrlKey && e.keyCode == 13 ) {
     e.preventDefault();
     e.stopImmediatePropagation();
     return false;
     }
     });*/


    jQuery('.input-group > .ui-inputfield').off('.telve').on('keypress.telve', function (e) {
        if (!e.ctrlKey && e.keyCode == 13) {
            e.preventDefault();
            e.stopImmediatePropagation();
            return false;
        } else if (e.ctrlKey && e.keyCode == 10) {

            if (jQuery(e.target).parent().hasClass('input-group') || jQuery(e.target).parent().parent().hasClass('input-group')) {
                jQuery(e.target).parent().parent().find('button:first').click();
                e.preventDefault();
                e.stopImmediatePropagation();
                return false;
            }
        } else if (e.ctrlKey && e.keyCode == 13) {

            if (jQuery(e.target).parent().hasClass('input-group') || jQuery(e.target).parent().parent().hasClass('input-group')) {
                jQuery(e.target).parent().parent().find('button:first').click();
                e.preventDefault();
                e.stopImmediatePropagation();
                return false;
            }
        }
    });

}

//Source : http://jsfiddle.net/333gu11u/
$.event.special.widthChanged = {
    remove: function () {
        $(this).children('iframe.width-changed').remove();
    },
    add: function () {
        var elm = $(this);
        var iframe = elm.children('iframe.width-changed');
        if (!iframe.length) {
            iframe = $('<iframe/>').addClass('width-changed').prependTo(this);
        }
        var oldWidth = elm.width();
        function elmResized() {
            var width = elm.width();
            if (oldWidth != width) {
                elm.trigger('widthChanged', [width, oldWidth]);
                oldWidth = width;
            }
        }

        var timer = 0;
        var ielm = iframe[0];
        (ielm.contentWindow || ielm).onresize = function () {
            clearTimeout(timer);
            timer = setTimeout(elmResized, 20);
        };
    }
}

function quickPanelActivate() {
    $(".content-wrapper").on('widthChanged', function () {
        quickPanelResize();
    });
}

function quickPanelResize() {
    var panelWidth = $(".content-wrapper").width();

    $('#quickpanel').css({'width': panelWidth});
}

function quickPanelToogle() {


    //Get the screen height and width
    quickPanelResize()

    jQuery('#quickpanel').toggle();
}



function maximazeDialog() {
    w = jQuery(parent.window).width();
    h = jQuery(parent.window).height();
    if (w <= 480 || h <= 640 ) {
        jQuery('.ui-dialog', parent.document).each(function () {
            wd = jQuery(this);
            var wv = wd.attr('data-widgetvar');

            if (wv != undefined) {
                //parent.PF(wv).toggleMaximize();
                w = jQuery(parent.window).width();
                h = jQuery(parent.window).height();// - 100;
                wd.css('height', h + 'px');
                wd.css('width', w + 'px');
                wd.css('top', '0px');
                wd.css('left', '0px');
                wd.css('z-index', '1035');

                iff = jQuery('iframe', this);
                h = h - 90;
                w = w - 30;
                iff.css('height', h + 'px');
                iff.css('width', w + 'px');
            }

        });
    }
}