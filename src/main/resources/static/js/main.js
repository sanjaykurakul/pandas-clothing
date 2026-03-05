function toggleMenu() {
    document.getElementById("sideMenu").classList.toggle("active");
    document.getElementById("overlay").classList.toggle("active");
}

/* ---------------- SIZE SELECTION ---------------- */

function selectSize(button) {

    const card = button.closest(".product-card");

    card.querySelectorAll(".size-btn")
        .forEach(btn => btn.classList.remove("active"));

    button.classList.add("active");

    card.dataset.selectedSize = button.innerText;
}

/* ---------------- ADD TO CART ---------------- */

function addToCart(productId, button) {

    const card = button.closest(".product-card");
    const selectedSize = card.dataset.selectedSize;

    if (!selectedSize) {
        alert("Please select size");
        return;
    }

    fetch(`/cart/add/${productId}?size=${selectedSize}`, {
        method: "POST"
    })
    .then(res => {
        if (res.status === 401) {
            window.location.href = "/account.html";
            return Promise.reject("Not logged in");
        }
        return res.text();
    })
    .then(data => {

            if (data) {
                alert(data);

                // 🔥 UPDATE CART COUNT INSTANTLY
                incrementCartCount();
            }

        })
        .catch(() => {});
    }

/* ---------------- ADD TO WISHLIST ---------------- */

function addToWishlist(productId, button) {

    const card = button.closest(".product-card");
    const selectedSize = card.dataset.selectedSize;

    if (!selectedSize) {
        alert("Please select size");
        return;
    }

    fetch(`/wishlist/add/${productId}?size=${selectedSize}`, {
        method: "POST"
    })
    .then(res => {

        if (res.status === 401) {
            window.location.href = "/account.html";
            return;
        }

        return res.text();
    })
    .then(data => {
        if (data) alert(data);
    });
}
/* ---------------- COUNTS ---------------- */

function loadWishlistCount() {
    fetch("/wishlist/my")
        .then(res => res.json())
        .then(data => {
            document.getElementById("wishlist-count").innerText = data.length;
        });
}

function loadCartCount() {
    fetch("/cart/my")
        .then(res => res.json())
        .then(data => {
            document.getElementById("cart-count").innerText = data.length;
        });
}

function checkUserStatus() {

    fetch("/current-user")
    .then(res => {
        if (res.status === 401) return null;
        return res.text();
    })
    .then(user => {

        const profileIcon = document.getElementById("profileIcon");
        const dropdown = document.getElementById("dropdown");

        if (!profileIcon) return;

        if (!user) {

            profileIcon.innerText = "Login";
            profileIcon.onclick = () => {
                window.location.href = "/account.html";
            };

            if (dropdown) dropdown.style.display = "none";

        } else {

            profileIcon.innerText = user + " ▾";
            profileIcon.onclick = toggleProfileMenu;

            loadCartCount();
            loadWishlistCount();
        }
    });
}
function logoutUser() {

    fetch("/logout", {
        method: "POST"
    })
    .then(() => {
        window.location.href = "/index.html";
    });
}
function incrementCartCount() {

    const cartBadge = document.getElementById("cart-count");

    if (!cartBadge) return;

    let currentCount = parseInt(cartBadge.innerText) || 0;

    cartBadge.innerText = currentCount + 1;
}
function toggleProfileMenu() {
    const drop = document.getElementById("dropdown");
    if (drop) {
        drop.style.display = drop.style.display === "block" ? "none" : "block";
    }
}

function logoutUser() {
    fetch("/logout", { method: "POST" })
        .then(() => {
            window.location.href = "/index.html";
        });
}

checkUserStatus();
loadWishlistCount();
loadCartCount();