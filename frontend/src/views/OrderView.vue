<!--
  This example requires some changes to your config:

  ```
  // tailwind.config.js
  module.exports = {
    // ...
    plugins: [
      // ...
      require('@tailwindcss/forms'),
      require('@tailwindcss/aspect-ratio'),
    ],
  }
  ```
-->
<template>
  <div class="bg-white">
    <!-- Mobile menu -->
    <TransitionRoot as="template" :show="open">
      <Dialog class="relative z-40 lg:hidden" @close="open = false">
        <TransitionChild as="template" enter="transition-opacity ease-linear duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="transition-opacity ease-linear duration-300" leave-from="opacity-100" leave-to="opacity-0">
          <div class="fixed inset-0 bg-black bg-opacity-25" />
        </TransitionChild>

        <div class="fixed inset-0 z-40 flex">
          <TransitionChild as="template" enter="transition ease-in-out duration-300 transform" enter-from="-translate-x-full" enter-to="translate-x-0" leave="transition ease-in-out duration-300 transform" leave-from="translate-x-0" leave-to="-translate-x-full">
            <DialogPanel class="relative flex w-full max-w-xs flex-col overflow-y-auto bg-white pb-12 shadow-xl">
              <div class="flex px-4 pb-2 pt-5">
                <button type="button" class="-m-2 inline-flex items-center justify-center rounded-md p-2 text-gray-400" @click="open = false">
                  <span class="sr-only">Close menu</span>
                  <XMarkIcon class="h-6 w-6" aria-hidden="true" />
                </button>
              </div>

              <!-- Links -->
              <TabGroup as="div" class="mt-2">
                <div class="border-b border-gray-200">
                  <TabList class="-mb-px flex space-x-8 px-4">
                    <Tab as="template" v-for="category in navigation.categories" :key="category.name" v-slot="{ selected }">
                      <button :class="[selected ? 'border-indigo-600 text-indigo-600' : 'border-transparent text-gray-900', 'flex-1 whitespace-nowrap border-b-2 px-1 py-4 text-base font-medium']">{{ category.name }}</button>
                    </Tab>
                  </TabList>
                </div>
                <TabPanels as="template">
                  <TabPanel v-for="category in navigation.categories" :key="category.name" class="space-y-12 px-4 py-6">
                    <div class="grid grid-cols-2 gap-x-4 gap-y-10">
                      <div v-for="item in category.featured" :key="item.menu_name" class="group relative">
                        <div class="aspect-h-1 aspect-w-1 overflow-hidden rounded-md bg-gray-100 group-hover:opacity-75">
                          <img :src="item.imageSrc" :alt="item.imageAlt" class="object-cover object-center" />
                        </div>
                        <a :href="item.href" class="mt-6 block text-sm font-medium text-gray-900">
                          <span class="absolute inset-0 z-10" aria-hidden="true" />
                          {{ item.menu_name }}
                        </a>
                        <p aria-hidden="true" class="mt-1 text-sm text-gray-500">Shop now</p>
                      </div>
                    </div>
                  </TabPanel>
                </TabPanels>
              </TabGroup>

              <div class="space-y-6 border-t border-gray-200 px-4 py-6">
                <div v-for="page in navigation.pages" :key="page.name" class="flow-root">
                  <a :href="page.href" class="-m-2 block p-2 font-medium text-gray-900">{{ page.name }}</a>
                </div>
              </div>

              <div class="space-y-6 border-t border-gray-200 px-4 py-6">
                <div class="flow-root">
                  <a href="#" class="-m-2 block p-2 font-medium text-gray-900">Create an account</a>
                </div>
                <div class="flow-root">
                  <a href="#" class="-m-2 block p-2 font-medium text-gray-900">Sign in</a>
                </div>
              </div>

              <div class="space-y-6 border-t border-gray-200 px-4 py-6">
                <!-- Currency selector -->
                <form>
                  <div class="inline-block">
                    <label for="mobile-currency" class="sr-only">Currency</label>
                    <div class="group relative -ml-2 rounded-md border-transparent focus-within:ring-2 focus-within:ring-white">
                      <select id="mobile-currency" name="currency" class="flex items-center rounded-md border-transparent bg-none py-0.5 pl-2 pr-5 text-sm font-medium text-gray-700 focus:border-transparent focus:outline-none focus:ring-0 group-hover:text-gray-800">
                        <option v-for="currency in currencies" :key="currency">{{ currency }}</option>
                      </select>
                      <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center">
                        <ChevronDownIcon class="h-5 w-5 text-gray-500" aria-hidden="true" />
                      </div>
                    </div>
                  </div>
                </form>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </Dialog>
    </TransitionRoot>

    <header class="relative">
      <nav aria-label="Top">


        <!-- Secondary navigation -->
        <div class="bg-white">
          <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            <div class="border-b border-gray-200">
              <div class="flex h-16 items-center justify-between">
                <!-- Logo (lg+) -->
                <div class="hidden lg:flex lg:flex-1 lg:items-center">
                  <a href="#">
                    <span class="sr-only">Your Company</span>
                    <img class="h-8 w-auto" src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600" alt="" />
                  </a>
                </div>

                <div class="hidden h-full lg:flex">
                  <!-- Flyout menus -->
                  <PopoverGroup class="inset-x-0 bottom-0 px-4">
                    <div class="flex h-full justify-center space-x-8">
                      <Popover v-for="category in navigation.categories" :key="category.name" class="flex" v-slot="{ open }">
                        <div class="relative flex">
                          <PopoverButton @click="getMenu(category.id)" :class="[open ? 'border-indigo-600 text-indigo-600' : 'border-transparent text-gray-700 hover:text-gray-800', 'relative z-10 -mb-px flex items-center border-b-2 pt-px text-sm font-medium transition-colors duration-200 ease-out']">{{ category.name }}</PopoverButton>
                        </div>

                        <transition enter-active-class="transition ease-out duration-200" enter-from-class="opacity-0" enter-to-class="opacity-100" leave-active-class="transition ease-in duration-150" leave-from-class="opacity-100" leave-to-class="opacity-0">
                          <PopoverPanel class="absolute inset-x-0 top-full text-sm text-gray-500">
                            <!-- Presentational element used to render the bottom shadow, if we put the shadow on the actual panel it pokes out the top, so we use this shorter element to hide the top of the shadow -->
                            <div class="absolute inset-0 top-1/2 bg-white shadow" aria-hidden="true" />

                            <div class="relative bg-white">
                              <div class="mx-auto max-w-7xl px-8">
                                <div class="grid grid-cols-4 gap-x-8 gap-y-10 py-16">
                                  <div @click="addToCart(item)" v-for="item in category.featured" :key="item.menu_name" class="group relative">
                                    <div class="aspect-h-1 aspect-w-1 overflow-hidden rounded-md bg-gray-100 group-hover:opacity-75">
                                      <img src="https://tailwindui.com/img/ecommerce-images/home-page-02-edition-03.jpg" :alt="item.imageAlt" class="object-cover object-center" />
                                    </div>
                                    <a class="mt-4 block font-medium text-gray-900">
                                      <span class="absolute inset-0 z-10" aria-hidden="true" />
                                      {{ item.menu_name }}
                                    </a>
                                    <p aria-hidden="true" class="mt-1">{{ item.price }}원</p>
                                    <div  class="mt-6 z-10">
                                      <div class="relative flex items-center justify-center rounded-md border border-transparent bg-gray-100 px-8 py-2 text-sm font-medium text-gray-900 hover:bg-gray-200"
                                      >주문에 추가<span class="sr-only">, {{ item.menu_name }}</span></div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </PopoverPanel>
                        </transition>
                      </Popover>

                      <a v-for="page in navigation.pages" :key="page.name" :href="page.href" class="flex items-center text-sm font-medium text-gray-700 hover:text-gray-800">{{ page.name }}</a>
                    </div>
                  </PopoverGroup>
                </div>
                <OrderCart/>
                <!-- Mobile menu and search (lg-) -->
                <div class="flex flex-1 items-center lg:hidden">
                  <button type="button" class="-ml-2 rounded-md bg-white p-2 text-gray-400" @click="open = true">
                    <span class="sr-only">Open menu</span>
                    <Bars3Icon class="h-6 w-6" aria-hidden="true" />
                  </button>


                </div>

                <!-- Logo (lg-) -->
                <a href="#" class="lg:hidden">
                  <span class="sr-only">Your Company</span>
                  <img src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600" alt="" class="h-8 w-auto" />
                </a>

                <div class="flex flex-1 items-center justify-end">

                  <div class="flex items-center lg:ml-8">
                    <!-- Help -->
                    <a href="#" class="p-2 text-gray-400 hover:text-gray-500 lg:hidden">
                      <span class="sr-only">Help</span>
                      <QuestionMarkCircleIcon class="h-6 w-6" aria-hidden="true" />
                    </a>
                    <a href="#" class="hidden text-sm font-medium text-gray-700 hover:text-gray-800 lg:block">Help</a>

                    <!-- Cart -->
                    <div @click="toggleCart" class="ml-4 flow-root lg:ml-8">
                      <div class="group -m-2 flex items-center p-2">
                        <ShoppingBagIcon class="h-6 w-6 flex-shrink-0 text-gray-400 group-hover:text-gray-500" aria-hidden="true" />
                        <span class="ml-2 text-sm font-medium text-gray-700 group-hover:text-gray-800">{{cartStore.cartCount}}</span>
                        <span class="sr-only">items in cart, view bag</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </nav>
    </header>
  </div>

</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {
  Dialog,
  DialogPanel,
  Popover,
  PopoverButton,
  PopoverGroup,
  PopoverPanel,
  Tab,
  TabGroup,
  TabList,
  TabPanel,
  TabPanels,
  TransitionChild,
  TransitionRoot,
} from '@headlessui/vue'
import {
  Bars3Icon,
  MagnifyingGlassIcon,
  QuestionMarkCircleIcon,
  ShoppingBagIcon,
  XMarkIcon,
} from '@heroicons/vue/24/outline'
import { ChevronDownIcon } from '@heroicons/vue/20/solid'
import {useCartStore} from "@/stores/cart.js";
import OrderCart from "@/components/OrderCart.vue";
import axios from "axios";

const navigation = ref({
  categories: [
    {
      id:1,
      name: '세트메뉴',
      featured: [
        {
          id: 1,
          name: 'New Arrivals',
          href: '#',
          price: 11000,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-category-01.jpg',
          imageAlt: 'Models sitting back to back, wearing Basic Tee in black and bone.',
        },
        {
          id: 2,
          name: 'Basic Tees',
          href: '#',
          price: 110,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-category-02.jpg',
          imageAlt: 'Close up of Basic Tee fall bundle with off-white, ochre, olive, and black tees.',
        },
        {
          id: 3,
          name: 'Accessories',
          href: '#',
          price: 110,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-category-03.jpg',
          imageAlt: 'Model wearing minimalist watch with black wristband and white watch face.',
        },
        {
          id: 4,
          name: 'Carry',
          href: '#',
          price: 110,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-category-04.jpg',
          imageAlt: 'Model opening tan leather long wallet with credit card pockets and cash pouch.',
        },
      ],
    },
    {
      id:2,
      name: '사이드',
      featured: [
        {
          id: 5,
          name: 'New Arrivals',
          href: '#',
          price: 110,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-01-men-category-01.jpg',
          imageAlt: 'Hats and sweaters on wood shelves next to various colors of t-shirts on hangers.',
        },
        {
          id: 6,
          name: 'Basic Tees',
          href: '#',
          price: 110,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-01-men-category-02.jpg',
          imageAlt: 'Model wearing light heather gray t-shirt.',
        },
        {
          id: 7,
          name: 'Accessories',
          href: '#',
          price: 110,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-01-men-category-03.jpg',
          imageAlt:
              'Grey 6-panel baseball hat with black brim, black mountain graphic on front, and light heather gray body.',
        },
        {
          id: 8,
          name: 'Carry',
          href: '#',
          price: 110,
          imageSrc: 'https://tailwindui.com/img/ecommerce-images/mega-menu-01-men-category-04.jpg',
          imageAlt: 'Model putting folded cash into slim card holder olive leather wallet with hand stitching.',
        },
      ],
    },
  ]
});

const HOST = "http://localhost:8088";
const cartStore = useCartStore()

const toggleCart = () => {
  cartStore.toggleCart()
}
const addToCart = (item) => {
  cartStore.addToCart(item);
};

const getMenu = async (category_id) => {
  try {
    const response = await axios.get(`${HOST}/menus/search/findByStoreIdAndCategoryId?storeId=1&categoryId=${category_id}`);
    if (response.status === 200) {
      console.log(response.data);
      // 해당 카테고리의 메뉴 목록을 가져와서 featured 배열에 맞게 매핑
      const category = navigation.value.categories.find(category => category.id === category_id);
      category.featured = response.data._embedded.menus.map(menu => ({
        id: menu._links.self.href.split('/').pop(), // 메뉴 ID는 self 링크에서 추출
        menu_name: menu.menuName,   // 메뉴 이름
        price: menu.menuPrice,      // 메뉴 가격
        imageSrc: 'https://placekitten.com/200/200', // 메뉴 이미지 (임시 이미지)
        imageAlt: `${menu.menuName} 이미지`,  // 이미지 대체 텍스트
        href: menu._links.self.href  // 메뉴 상세보기 링크
      }));
    }
  } catch (error) {
    console.error('메뉴를 불러오는 중 오류 발생:', error);
  } finally {
    console.log('항상 마지막에 실행');
  }
};
onMounted(() => {
  axios.get(`${HOST}/categories?storeId=1`)
    .then(response => {
      if (response.status === 200) {
        // 응답 데이터에서 카테고리 정보를 가져옴
        const categoriesData = response.data._embedded.categories;
        // 카테고리를 매핑하여 필요한 속성만 추출
        navigation.value.categories = categoriesData.map(category => ({
          id: category._links.self.href.split('/').pop(), // id는 URL의 마지막 부분에서 추출
          name: category.categoryName,  // 카테고리 이름
          href: category._links.self.href,  // self 링크
          featured: []  // 초기 featured는 빈 배열로 설정, 나중에 채워질 수 있음
        }));
      }
    })
    .catch(error => {
      console.error('get category error: ', error);
    });
  const storeName = localStorage.getItem('store_name');
  if (!storeName) {
    alert("메뉴를 불러오지 못했습니다. 로그인해서 가게를 알려주세요.")
    window.location.href = '/login';
  }
});

const open = ref(false)
</script>