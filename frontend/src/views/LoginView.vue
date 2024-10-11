<template>
  <!--
     This example requires updating your template:

     ```
     <html class="h-full bg-white">
     <body class="h-full">
     ```
   -->
  <div class="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
      <!-- <img class="mx-auto h-10 w-auto" src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600" alt="Your Company" /> -->
      <h2 class="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">신청하신 아이디로 로그인 해주세요</h2>
    </div>

    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <div class="space-y-6">
        <div>
          <label for="username" class="block text-sm font-medium leading-6 text-gray-900">아이디</label>
          <div class="mt-2">
            <input id="username" v-model="username" name="username" required="" class="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <div class="flex items-center justify-between">
            <label for="password" class="block text-sm font-medium leading-6 text-gray-900">비밀번호</label>
            <div class="text-sm">

            </div>
          </div>
          <div class="mt-2">
            <input id="password" v-model="password" name="password" type="password" autocomplete="current-password" required="" class="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <button @click="login" class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">로그인</button>
        </div>
      </div>

      <p class="mt-10 text-center text-sm text-gray-500">
        회원이 아니신가요?
        {{ ' ' }}
        <a href="#" class="font-semibold leading-6 text-indigo-600 hover:text-indigo-500">가입 신청하기</a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const username = ref('');
const password = ref('');
const router = useRouter();

const login = async () => {
  try {
    const response = await axios.post(axios.fixUrl("http://localhost:8088/users/login"), {
    username: username.value,
    password: password.value
  });

    if (response.status === 200) {
      localStorage.setItem('store_name', response.data.store_name);
      router.push({ name: 'Main' }).then(() => {
        // Main 페이지로 이동한 후 App.vue의 onMounted 실행
        location.reload();
      });
    }
  } catch (error) {
    console.error('Login error:', error.response ? error.response.data : error.message);
  } finally {
    console.log('항상 마지막에 실행', error.response ? error.response.data : error.message);
  }
};

if (localStorage.getItem('store_name')) {
  router.push({ name: 'Main' });
}
</script>

<style scoped>
/* 스타일 추가 가능 */
</style>
