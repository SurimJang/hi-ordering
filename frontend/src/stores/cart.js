import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', {
    state: () => ({
        open: false,
        cartItems: [],
        cartCount: 0,
    }),
    actions: {
        toggleCart() {
            this.open = !this.open
            console.log(this.open);
        },
        openCart() {
            this.open = true
        },
        resetCart() {
            this.cartItems = [];
            this.cartCount = 0;
        },
        closeCart() {
            this.open = false
        },
        addToCart(item) {
            // cartItems 배열에서 아이템의 id로 찾기
            const existingItem = this.cartItems.find(cartItem => cartItem.id === item.id);

            if (existingItem) {
                // 아이템이 이미 존재하면 qty를 증가시킴
                existingItem.qty += 1;
            } else {
                // 아이템이 존재하지 않으면 새로 추가
                this.cartItems.push({...item, qty: 1});
            }

            this.cartCount = this.cartItems.length;
        },
        removeFromCart(item) {
            console.log(this.cartItems);
            this.cartItems = this.cartItems.filter(cartItem => cartItem.id !== item.id);
            this.cartCount = this.cartItems.length;
        },
    }
})
