// ================= LOAD PRODUCTS =================

document.addEventListener("DOMContentLoaded", function () {

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    updateCartCount();

    fetch("/api/products")
        .then(response => response.json())
        .then(products => {
            const container = document.getElementById("product-container");

            if (!container) return;

            products.forEach(product => {
                const div = document.createElement("div");
                div.className = "product-card";

                div.innerHTML = `
                    <img src="/images/${product.imageUrl}" />
                    <h3>${product.name}</h3>
                    <p>&#8377;${product.price}</p>
                    <button class="add-btn">Add to Bag</button>
                `;

                div.querySelector(".add-btn")
                    .addEventListener("click", function () {
                        addToCart(product);
                    });

                container.appendChild(div);
            });
        });

});


// ================= ADD TO CART =================

function addToCart(product) {

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    cart.push(product);

    localStorage.setItem("cart", JSON.stringify(cart));

    updateCartCount();

    alert(product.name + " added to cart 🛒");
}


// ================= UPDATE CART COUNT =================

function updateCartCount() {

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    const cartElement = document.querySelector(".cart-count");

    if (cartElement) {
        cartElement.innerText = cart.length;
    }

}

let wishlistCount = localStorage.getItem("wishlistCount")
    ? parseInt(localStorage.getItem("wishlistCount"))
    : 0;

const wishlistButtons = document.querySelectorAll(".wishlist-btn");

wishlistButtons.forEach(btn => {
    btn.addEventListener("click", function () {
        wishlistCount++;
        localStorage.setItem("wishlistCount", wishlistCount);
        alert("Added to wishlist ❤️");
    });
});