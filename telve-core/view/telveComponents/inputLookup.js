/**
 * Normal text alandan alıp hidden alana koyar ki form post ile değer bean'e gitsin.
 * @param {type} sourceId
 * @param {type} targetId
 * @returns {undefined}
 */
function setLookSearchText(sourceId, targetId) {
    var sid = PrimeFaces.escapeClientId(sourceId);
    var tid = PrimeFaces.escapeClientId(targetId);

    jQuery(tid).val(jQuery(sid).val());
    
}