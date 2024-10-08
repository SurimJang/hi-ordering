<template>
    <div>
      <h1>Payment Page</h1>
      <form @submit.prevent="requestPayment">
        <label>Product Name:</label>
        <input v-model="productName" type="text" required />
  
        <label>Amount:</label>
        <input v-model="amount" type="number" required />
  
        <button type="submit">Pay Now</button>
      </form>
    </div>
    {{ response }}
  </template>
  
  <script>
  import PortOne from '@portone/browser-sdk/v2';
  export default {
  data() {
    return {
      orderName: "나이키 와플 트레이너 2 SD",
      totalAmount: 1000,
      paymentResponse: null,
    };
  },
  methods: {
    async requestPayment() {
      try {
        const response = await PortOne.requestPayment({
          storeId: "store-879407b7-ff64-43b1-afbc-b976ed614925",
          channelKey: "channel-key-14c3c73d-7978-4206-97f0-d272dd2c25f6",
          paymentId: `payment${Math.random().toString(36).slice(2)}`, // 유니크한 ID 생성
          orderName: this.orderName,
          totalAmount: this.totalAmount,
          currency: "CURRENCY_KRW",
          payMethod: "CARD",
        });
        this.paymentResponse = response;
      } catch (error) {
        console.error("결제 요청 실패:", error);
      }
    },
  },
};
</script>