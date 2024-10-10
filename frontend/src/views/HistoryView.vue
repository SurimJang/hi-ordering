<template>
    <div class="bg-gray-900">
      <div class="mx-auto max-w-7xl">
        <div class="grid grid-cols-1 gap-px bg-white/5 sm:grid-cols-2 lg:grid-cols-4">
          <div v-for="(order_history, idx) in order_histories" :key="order_history.createdAt" class="bg-gray-900 px-4 py-6 sm:px-6 lg:px-8">
            <p class="text-sm font-medium leading-6 text-gray-400">{{ formatDate(order_history.createdAt) }}</p>
            <p class="mt-2 flex items-baseline gap-x-2">
              <span class="text-xl font-semibold tracking-tight text-white">
                <!-- orderMenus 배열에서 각 메뉴의 정보를 출력 -->
                <div v-for="(menu, menuIdx) in order_history.orderMenus" :key="menuIdx">
                  {{ getMenuNameById(menu.menuId) }}
                  <div>- {{ menu.qty }}개, 총 가격: {{ getMenuPriceById(menu.menuId) * menu.qty }}원</div>
                </div>
              </span>
            </p>
            <div class="text-sm text-gray-400">결제 금액 : {{ order_history.paymentAmount }}원</div>
            <div class="text-sm text-red-300">{{ order_history.orderStatus === "OrderConfirmed"? "주문 완료됨" : "주문 취소됨" }}</div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  
  const HOST = "http://localhost:8088";
  const order_histories = ref([]);
  const menus = ref([]); // 메뉴 정보를 저장할 배열
  
  // 날짜 포맷터 함수
  function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
  }
  
  // href에서 ID를 추출하는 함수
  function extractIdFromHref(href) {
    const parts = href.split('/');
    return parts[parts.length - 1]; // URI의 마지막 부분이 ID
  }
  
  // 메뉴 ID로 메뉴 이름을 찾아 반환하는 함수
  function getMenuNameById(menuId) {
    const menu = menus.value.find(m => extractIdFromHref(m._links.self.href) === String(menuId));
    return menu ? menu.menuName : '알 수 없는 메뉴';
  }

  // 메뉴 ID로 메뉴 가격을 찾아 반환하는 함수
function getMenuPriceById(menuId) {
  const menu = menus.value.find(m => extractIdFromHref(m._links.self.href) === String(menuId));
  return menu ? menu.menuPrice : 0; // 가격이 없으면 0을 반환
}

  
  onMounted(() => {
    // store_id가 1인 메뉴를 먼저 가져오는 요청
    axios.get(`${HOST}/menus?store_id=1`)
      .then(response => {
        if (response.status === 200) {
          menus.value = response.data._embedded.menus;
          
          // 메뉴 데이터를 가져온 후 주문 내역을 가져오는 요청
          axios.get(`${HOST}/orders?sort=createdAt,desc`)
            .then(response => {
              if (response.status === 200) {
                const orders = response.data._embedded.orders;
  
                // order_histories에 orders 데이터를 저장
                order_histories.value = orders.map(order => {
                  // 각 order에서 메뉴의 ID와 수량을 처리
                  order.orderMenus = order.orderMenus.map(menu => {
                    return {
                      menuId: menu.menuId, // menuId 사용
                      qty: menu.qty        // 주문 수량
                    };
                  });
                  return order;
                });
              }
            })
            .catch(error => {
              console.error('Error fetching orders:', error);
            });
        }
      })
      .catch(error => {
        console.error('Error fetching menus:', error);
      });
  });
  </script>