<script lang="ts" setup>
    import { ref, onMounted } from 'vue';

    const emojihistory = ref<string[]>([]);
    const emit = defineEmits(['emojiChick']);
    const emoji = ref([
    "0161251e1e8547b2a3af25089edfac36.png",
    "03e08f87351e4260a796c4955588051a.png",
    "049b6b6296a848a1bd91f0ef834fb99c.png",
    "05856733edd54e799830444e1dd9fa54.png",
    "07eff2c20ff64e61b99b7e964ace5e09.png",
    "0a48e3fbc4df44df9b354b0e69490a5c.png",
    "0c17badc0bc149648533adaf6b6bfd32.png",
    "0cc98167ba1a4306a06eeb7955b9b42a.png",
    "0d2c3e99dc4447388e7515b7b0ec614e.png",
    "0d89f19285eb4f2398992142e47b75a5.png",
    "11178378d32f4f23897226fac3297686.png",
    "134cf2c4d59f4a38b9276c51dfd7a74b.png",
    "14700019b3e54feea2d5c1e277a0ca15.png",
    "1793ae2ea76840adb45a949ed7f22341.png",
    "19c8643bdee94fcab31ffdaecb07c36e.png",
    "1af829c9a9e74be493c6f7b71ab084a2.png",
    "1b51743ac8e34517bdfc0be04cfc41c0.png",
    "1c56672a24fd44cc8a14097c1fc494b7.png",
    "2202bf7e1abf48ae9472de04ed07d291.png",
    "22277b70337c49fd958988e4fc5b57d2.png",
    "27e801c859ce43a380e6ea11cc534bf9.png",
    "28171e27ac114ddfadb24130d736c018.png",
    "281b5659e6fb46bebb2b29fb1b443643.png",
    "28f581e687cb4f2fa5ecdacb2f950ce8.png",
    "297206f55a6c4601b628b0a715f4556c.png",
    "2988a1ad6836462dbfcc3a744b1b0b63.png",
    "2d83e36151d0414fa1fc9594cc09396c.png",
    "2e66d09c3c3a41edad62c2da738b2cf7.png",
    "2f6db6d27c454aeaa02de0d78c66be9a.png",
    "329fefe6d32a431aaff4cbd200d11e33.png",
    "349f977ebb0a4ab18f9d2d5d983356f4.png",
    "34c190a46c5d4a82a4d0aa157d22f0e8.png",
    "3c590d9318ee4fc79e97eb96f749f40b.png",
    "3fc1d17fc64b430cb36ba4550d05490e.png",
    "429bdb6117a94f41a096647b86919d9f.png",
    "42b7d8c8f1a1441ba24ccf7bb92eb2d2.png",
    "43f807e6578c4075a84a65e24c504fd9.png",
    "460417fe91094bd3a3bf1ccc4b684410.png",
    "46f845c65f464d04877d4dc192d54b21.png",
    "485eda759a3f4b31a2f58f491c5e59a9.png",
    "48d992acf4bd44a0bb0214cf74065969.png",
    "494579968c884eb29b95e6ef3974a5c0.png",
    "495d62f615d145f88f844429d4812c47.png",
    "49d2ec98a3604d64a800fea37659287c.png",
    "4a5c7d97626b41e2adc995d1e7706716.png",
    "4b0310d429c440deaad4c3df45b4de80.png",
    "4bda2f1e65ac4245bef770cdc8e47238.png",
    "4cbc02f7d8f64d969137dd472eea156a.png",
    "4cc78732501f4e689bad1e094bdaf6c5.png",
    "4d4b1993f1a54c259f35a6becabce161.png",
    "4e3d0884727d442c91683d64bb8a8452.png",
    "4eb23ebaf0ed402fb0cd1eb963ec69e9.png",
    "50e60773304d4f65b7106509db4f4672.png",
    "5236c052af8646e89f8f7a3302414f80.png",
    "52f624af2ba84f43861ca6864e76fd99.png",
    "54e2c8bf602842e6b8d3fecd0423b94c.png",
    "5744e0952bb64c70b37262f7159a54b9.png",
    "577485f23b3e4eb6a9975871363b19ca.png",
    "577c1511329b4d21b67ea42fa42bf9b3.png",
    "57a98b7da8b247a38971738c045c91cb.png",
    "59d2169ca78e426fb9308c3ac5ddd5b2.png",
    "59fd7f5c36394c8190f946ea8c18671b.png",
    "5bc28788bf2b4454a4cc948b82e5cb2a.png",
    "6111858c99174b0fa4c670d2831dd1d9.png",
    "6135c3fd1aba48f8b95c74f58ed62cd2.png",
    "6186f010e5cc4b738066f15fb4cc75ad.png",
    "624c0b6e1c734573bf1c3d380e601e38.png",
    "6447acd5581d477cbf9d7a97cbd63d9b.png",
    "65f5c504f5474997b021da8fbe2a6e86.png",
    "693c552ae7f8471badc24a79204a1ec7.png",
    "695ae072f7d04f048eac7a7a9f17f634.png",
    "69916f6da3e44c5d8936f9ce686103bd.png",
    "69c3af9487ac46c7a159413edb670504.png",
    "6a2f80dba03942b9b257df4d7bcafc1e.png",
    "6c9e15b3c5db49129385b7b8b41cb973.png",
    "6fd1ac6ae78a4d0da6c82aa28da8a6ff.png",
    "76e54876ada144258edaece028fafd90.png",
    "7880ed2c054d4869bf803a907ef36c7a.png",
    "78ac030564a845aa8c49cb3c3e450cee.png",
    "796a061b234340798ec16211c2733614.png",
    "79d9b8bc2a0c4a0ba7dd47f71c16cb4a.png",
    "7a43d26cbaad479ab5544c881cbd9425.png",
    "7aff8a42439b45b9bd666f5120a8a2d5.png",
    "7b946a98d796456d866844c04a31e541.png",
    "7beee9967c324dff9a33276d065a2f89.png",
    "8011f836b94342ec926ce34ff63a456f.png",
    "804b37c4edfc445fb135e4e41cfbde65.png",
    "8194d06f31e842baae0364a1e4060b9e.png",
    "8315141d6f6344c5ba53b90c89997847.png",
    "851de0fe19824a2c83208aac8427cb9b.png",
    "86c1f0938b1943d79ac912854e1dffd9.png",
    "887b551c04654187bea874c7e485ed20.png",
    "88ecf7dbce9b487a920f170fa3da9bf5.png",
    "8968b906bc6d47cf9c6742c962311cb0.png",
    "89cb1d816e1147e5a81fd34d30e7a072.png",
    "8d30481567244a4a975b27b4322a0ab3.png",
    "8d7416827b554524a5963f2bd55224a0.png",
    "9388be70e26846a786e35f5be7e4fee6.png",
    "93e4a4dc11824f0ab37b77bd5c0b95ac.png",
    "941954622ed846f1b755e95fdc6c7b30.png",
    "959493883571483698077c91f68fe706.png",
    "95992dbe4175410790a3f631df0c7027.png",
    "96e786d9b3c547f7a026e8ecc981f2c3.png",
    "970eaac5200e4c278aff87932247f498.png",
    "9884444095364f799cf6a6e48dea0e46.png",
    "98f384be3f5b41c3b9bcea20741771ff.png",
    "9932b6c683a843b083d2425984b4469d.png",
    "9b0a2e5f9f6c438a8c5bb9210c4d9094.png",
    "9ca84b691b8b4c03a5ad4d8b643a744a.png",
    "9e8c22da52114b37b54d91cefa80cd31.png",
    "9eaa673b8f464c0c9542ff7987a07baa.png",
    "9f4c22bc6c664d00a2118c8629c874d9.png",
    "a132b4cf91b2408ba38e19949c84e64b.png",
    "a1be905ea41f4a1faf28916a76b7d53a.png",
    "a28c180847714575abd04d5bb148f3ed.png",
    "a4654e8ca83b40c79441f895ca462752.png",
    "a4dde53529884674bbb3b06ac44d29cf.png",
    "a548985c30074611a6e3e2316c02f9ae.png",
    "a7886967cf4e400a9b0ab91767f06335.png",
    "a7a1681f8ec64613af6a15b8c8ae4deb.png",
    "ab7e560a94c146608506fcd8bfcda74f.png",
    "ac1dbb6ac1ac4fac9109e161977fa8c5.png",
    "b20381e2ce3e4d05b8ab13d6853e2b18.png",
    "b2c14ffc13b74bb28f7621e25e8e15f4.png",
    "b624568db083405fa06c4a6c44826a92.png",
    "b73f23a384b144c4a734e87fcf1a1d82.png",
    "b74b188e021541f99feb13330ac38813.png",
    "b7b369c67f8641c39d4a4d22dc1e2389.png",
    "b835b43019324efe829a9f6cd52ffc4e.png",
    "b869ffdb59324b5bb4956274c608e4eb.png",
    "be95376c898248628ad6808acce4b5d9.png",
    "bf1310f348484bc8af57ef8a495da5c9.png",
    "c1efc93a5fda4ffc97df30e695c98f6f.png",
    "c2c435c65e58475c83a1a76780e14ced.png",
    "c3412e37f63f41e5b41ffe49b81ea751.png",
    "c5413ec325684ea8ba44b7c28e87a576.png",
    "c5a54267121141b8b220195697e1755b.png",
    "c7d55599bb01412f8706b9c9130753f9.png",
    "c7d8e5c927cc414b81b81848d53e33c0.png",
    "c8ee4f4f788a41f7aa924e18485b0f16.png",
    "c9806a4038864adc9ffeb7fd4df4aa23.png",
    "c9aecd96ceed4522ab42996800de5998.png",
    "cc21a7f57c1e49aea719fb2d4c85cf57.png",
    "cd07ccbc4a7043048bbc320a8f5229bf.png",
    "cde766dbded94bab8cac6f71e9690bc1.png",
    "cec957e3bac742bbb9880e8c18d041fa.png",
    "cf0f84789fdf4abfa6bfff8ddb070f36.png",
    "cfb1213034c542e4bf53179f18d461ca.png",
    "d22f5b2770af4d42b4092695452a063c.png",
    "d285f560227e4c449265ec56fa51f4b6.png",
    "d40bedeb90c44780a9d8fe3346a8241a.png",
    "d589efbef8f8437091675d14b1e23300.png",
    "d7af94570623404a98547c0a893e60fb.png",
    "d9819e1f5f2f445298beb78d78d1f2c1.png",
    "d9eb24c77eeb42e299884e4106adad79.png",
    "dd001390f8d84f1581446f657cdbcab8.png",
    "dd2975cbf6da413dbcb599bcf063f676.png",
    "df05750899e546fd829a2c4faf56c52c.png",
    "dfc4c13ff5794e4198c35a3ce0ff1e19.png",
    "e00e6bbed8944887928e4abca4eec226.png",
    "e02b2f77f4cd457dab6b14924510b501.png",
    "e1ccb75ca59d42c8b8bce998a9fa283d.png",
    "e48c902a72344623920bdb5d7097e501.png",
    "e71d00c3b30e4b3d94f04dad1fc9a8d3.png",
    "eb6549485a2e462285e10e15c88cd9b6.png",
    "eb9f619dead448ae97f00ed7b15c91b3.png",
    "edaa83379bf741b1a9b0f36d1e1505f9.png",
    "ee54c4619b384297ae5e951da1ee70f2.png",
    "f0137f566c7a4a679fe0b61685586cb2.png",
    "f0ff3747d4df43179e7edddfcba9a48f.png",
    "f15508d1daa34ece8c9f92e77d2c5313.png",
    "f22d75cdb49547668893c1bfd25bfba4.png",
    "f99b13c01d9a4604b0c793dbc8f8c3d6.png",
    "fd2d6b38a7224149a61125e9fc1968ea.png",
    "ff6a5a2d59d441d7a3210ff7c0370044.png"
    ]);

    function appendEmoji(i: string){
      let targetUrl = "/emoji/" + i;
      emit("emojiChick", targetUrl); // 请确保父组件中有对应的事件监听器
      saveHistory(i);
      restoreHistory();
    };

    function removeHistory(){
      window.localStorage.removeItem("emoji-history");
      restoreHistory();
    };

    function restoreHistory(){
      let record = window.localStorage.getItem("emoji-history");
      emojihistory.value = record ? record.split(",") : [];
    };

    function saveHistory(emojiIndex: string){
      let record = window.localStorage.getItem("emoji-history");
      let targetSaveStr = emojiIndex;
      let count = 1;
      if (record) {
        let splitArrays = record.split(",");
        for (let i = 0; i < splitArrays.length; i++) {
          let item = splitArrays[i];
          if (emojiIndex.toString() !== item) {
            targetSaveStr += ",";
            targetSaveStr += item;
            count++;
            if (count > 5) {
              break;
            }
          }
        }
      }
      window.localStorage.setItem("emoji-history", targetSaveStr);
    };

    onMounted(() => {
      restoreHistory();
    });
</script>

<template>
  <div class="emoji-panel-container">
      <div class="emoji-title" v-show="emojihistory.length !== 0">
        <span>最近使用</span>
        <span class="delete-history-btn" @click="removeHistory">删除历史</span>
      </div>
      <div class="emoji-history-container">
        <img v-for="i in emojihistory" :src="'/emoji/' + i"
          :key="i" class="emoji-item" @click="appendEmoji(i)">
      </div>

      <div class="emoji-title">
        全部表情
      </div>
      <div class="emoji-all-container">
        <img class="emoji-item" v-for="i in emoji" :src="'/emoji/' + i"
          :key="i" @click="appendEmoji(i)">
      </div>
  </div>
</template>

<style>
    .emoji-panel-container{
      width: 25vw;
      border: 0.1vh solid;
      border-radius: 0.5vh;
      box-shadow: 0.5vh 0.5vh 1vh rgba(29, 29, 29, 0.5); /* 水平偏移量 垂直偏移量 模糊半径 阴影颜色 */
      background-color: white;
    }

    .delete-history-btn{
      color: #0084FF;
      cursor: pointer;
    }

    .emoji-title{
      display: flex;
      justify-content: space-between;
    }

    .emoji-title {
      padding: 1vh;
    }

    /*滚动条效果*/
    .emoji-panel-container::-webkit-scrollbar{ 
      width: 1vw;
    }

    .emoji-panel-container::-webkit-scrollbar-track{
      background-color: #F9FAFB;
      -webkit-border-radius: 2em;
      -moz-border-radius: 2em;
      border-radius: 2em;
    }

    .emoji-panel-container::-webkit-scrollbar-thumb {
      background-color: #E5E6EB;
      -webkit-border-radius: 2em;
      -moz-border-radius: 2em;
      border-radius: 2em;
    }

    .emoji-panel-container {
      overflow-y: scroll;
      overflow-x: hidden;
      height: 20vh;
    }

    .emoji-item:hover{
      transform: scale(1.3);
      transition: transform .3s;
    }

    .emoji-item{
      width: 5vh;
      height: 5vh;
      transition: all .3s linear;
      padding: 1vh;
      cursor: pointer;
    }
</style>