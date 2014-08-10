
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
    jQuery(ids).each(function() {
        jQuery(this).enable();
    });
}

function disableRibbonBtn(ids) {
    jQuery(ids).each(function() {
        jQuery(this).disable();
    });
}