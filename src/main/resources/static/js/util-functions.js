window.onload = function () {
    document.getElementById("shareForm").style.display = 'none';
}


function showShareForm(passwordId, websiteName) {
    let form = document.getElementById("shareForm");
    //form.style.display = (form.style.display === 'none') ? 'block' : 'none';
    document.getElementById("passId").value = passwordId;
    document.getElementById("websiteName").textContent = websiteName;
    form.style.display = 'block';
}
