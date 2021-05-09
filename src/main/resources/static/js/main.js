function addValidationErrorMessage(referenceNode, newNode, newId) {
    if (newNode != null) {
        if (referenceNode != null) {
            referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
            newNode.id = newId;
        }
    }
}

function orderSelectpicker(element) {
    var note = element.find('option').first(),// the '--Select ...--' option
        selected = element.find('option:selected'),
        rest = element.find('option:not(:first-of-type):not(:selected)');

    var sorter = (a, b) => {
        let aName = a.innerText.toLowerCase(),
            bName = b.innerText.toLowerCase();
        return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
    };

    element
        // adding '--Select ...--'
        .html(note)
        // appending selected options sorted in alphabetical order
        .append(selected.sort(sorter))
        // adding the rest of the options sorted too
        .append(rest.sort(sorter));
    element.selectpicker('refresh');
}