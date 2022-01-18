window.onload = function () {
    document.getElementById("shareForm").style.display = 'none';
}


function showOrHidePasswordForm() {
    let form = document.getElementById("shareForm");
    form.style.display = (form.style.display === 'none') ? 'block' : 'none';
}
