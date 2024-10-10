<template>
  <TransitionRoot as="template" :show="cartStore.open">
    <Dialog class="relative z-10" @close="cartStore.closeCart">
      <TransitionChild as="template" enter="ease-in-out duration-500" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in-out duration-500" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
      </TransitionChild>

      <div class="fixed inset-0 overflow-hidden">
        <div class="absolute inset-0 overflow-hidden">
          <div class="pointer-events-none fixed inset-y-0 right-0 flex max-w-full pl-10">
            <TransitionChild as="template" enter="transform transition ease-in-out duration-500 sm:duration-700" enter-from="translate-x-full" enter-to="translate-x-0" leave="transform transition ease-in-out duration-500 sm:duration-700" leave-from="translate-x-0" leave-to="translate-x-full">
              <DialogPanel class="pointer-events-auto w-screen max-w-md">
                <div class="flex h-full flex-col overflow-y-scroll bg-white shadow-xl">
                  <div class="flex-1 overflow-y-auto px-4 py-6 sm:px-6">
                    <div class="flex items-start justify-between">
                      <DialogTitle class="text-lg font-medium text-gray-900">현재 선택한 메뉴</DialogTitle>
                      <div class="ml-3 flex h-7 items-center">
                        <button type="button" class="relative -m-2 p-2 text-gray-400 hover:text-gray-500" @click="cartStore.closeCart">
                          <span class="absolute -inset-0.5" />
                          <span class="sr-only">Close panel</span>
                          <XMarkIcon class="h-6 w-6" aria-hidden="true" />
                        </button>
                      </div>
                    </div>

                    <div class="mt-8">
                      <OrderModal :open="modalOpen" @close="modalOpen = false" />
                      <div class="flow-root">
                        <ul role="list" class="-my-6 divide-y divide-gray-200">
                          <li v-for="product in products" :key="product.id" class="flex py-6">
                            <div class="h-24 w-24 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
                              <img src="https://github.com/user-attachments/assets/6f88629c-8548-45ba-a7e2-74fae3cbf93d" :alt="product.imageAlt" class="h-full w-full object-cover object-center" />
                            </div>
                          
                            <div class="ml-4 flex flex-1 flex-col">
                              <div>
                                <div class="flex justify-between text-base font-medium text-gray-900">
                                  <h3>
                                    <a :href="product.href">{{ product.name }}</a>
                                  </h3>
                                  <p class="ml-4">{{ formatPrice(product.price) }} 원</p>
                                </div>
                                <p class="mt-1 text-sm text-gray-500">{{ product.color }}</p>
                              </div>
                              <div class="flex flex-1 items-end justify-between text-sm">
                                <label class="text-gray-500">수량
                                  <select v-model="product.qty" class="max-w-full rounded-md border border-gray-300 py-1.5 text-left text-base font-medium leading-5 text-gray-700 shadow-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500 sm:text-sm">
                                    <option v-for="n in 8" :key="n" :value="n">{{ n }}</option>
                                  </select>
                                </label>
                                <div class="flex" @click="removeProduct(product)">
                                  <button type="button" class="font-medium text-indigo-600 hover:text-indigo-500"  >주문에서 제거</button>
                                </div>
                              </div>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </div>
                  </div>

                  <div class="border-t border-gray-200 px-4 py-6 sm:px-6">
                    <div class="flex justify-between text-base font-medium text-gray-900">
                      <p>총액</p>
                      <p>{{ formatPrice(subtotal) }} 원</p>
                    </div>
                    <p class="mt-0.5 text-sm text-gray-500">다시 한 번 주문하신 메뉴를 확인해주세요.</p>
                    <div class="mt-6">
                      <a @click="confirm" class="flex items-center justify-center rounded-md border border-transparent bg-indigo-600 px-6 py-3 text-base font-medium text-white shadow-sm hover:bg-indigo-700">이대로 주문할게요</a>
                    </div>
                    <div class="mt-6 flex justify-center text-center text-sm text-gray-500">
                      <p>
                        or{{ ' ' }}
                        <button type="button" class="font-medium text-indigo-600 hover:text-indigo-500" @click="cartStore.closeCart">
                          돌아가기
                          <span aria-hidden="true"> &rarr;</span>
                        </button>
                      </p>
                    </div>
                  </div>
                </div>
              </DialogPanel>
            </TransitionChild>
          </div>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>

</template>

<script setup>
import {computed, ref} from 'vue'
import { Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'
import { XMarkIcon } from '@heroicons/vue/24/outline'
import { useCartStore } from '../stores/cart.js'
import axios from "axios";
import OrderModal from "@/components/OrderModal.vue";

const modalOpen = ref(false)
const HOST = "http://localhost:8088"
const cartStore = useCartStore();
const products = computed(() => cartStore.cartItems);
const removeProduct = (product) => {
  cartStore.removeFromCart(product);
};

// 총액 계산하는 computed 속성
const subtotal = computed(() => {
  console.log(products.value);
  return products.value.reduce((total, product) => {
    return total + (product.price * product.qty);
  }, 0);
});

const confirm = () => {
  const orderDetails = products.value.map(product => ({
    menuId: product.id,  // 메뉴 ID
    qty: product.qty     // 수량
  }));

  const payload = {
    userId: 1,  // 유저 ID, 필요시 동적으로 변경 가능
    paymentAmount: subtotal.value,  // 총 결제 금액
    orderMenus: orderDetails,  // 주문한 메뉴 목록
    orderStatus: "placed"  // 주문 상태
  };

  // axios로 서버에 주문 생성 요청
  axios.post(axios.fixUrl('/orders'), payload)
    .then(response => {
      if (response.status === 201) {
        console.log('Order confirmed:', response.data);
        // 주문이 완료되면 장바구니를 리셋하고 모달을 염
        cartStore.resetCart();
        modalOpen.value = true;
      }
    })
    .catch(error => {
      console.error('Order confirmation error:', error);
      // 오류 처리 로직을 추가적으로 작성할 수 있음
    });
};

// const confirm = () => {
//   const orderDetails = products.value.map(product => (
//     {
//     menuId: product.id,
//     qty: product.qty
//   }
// ));

//   const payload = {
//     order_items: orderDetails,
//     payment_amount: subtotal.value
//   };

//   axios.post(`${HOST}/api/order/confirm`, payload)
//       .then(response => {
//         if (response.status === 201) {
//           console.log('Order confirmed:', response.data);
//           // 주문이 성공적으로 완료된 후 장바구니를 비우거나 사용자가 알림을 받도록 추가 작업을 수행할 수 있습니다.
//           cartStore.resetCart();
//           modalOpen.value = true;
//         }
//       })
//       .catch(error => {
//         console.error('Order confirmation error:', error);
//         // 오류 처리 로직을 추가할 수 있습니다. 예를 들어, 사용자에게 오류 메시지를 표시하는 것 등.
//       });
// };

// 숫자를 "8,000"과 같이 포맷하는 함수
const formatPrice = (price) => {
  return new Intl.NumberFormat('ko-KR').format(price);
};

</script>
