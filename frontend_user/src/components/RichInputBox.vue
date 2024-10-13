<script setup lang="ts">
    import { ref, onMounted, watch} from 'vue';

    const props = defineProps({
      content: {
        type: String,
        required: true
      }
    });

    const emit = defineEmits(['update:content']);

    const hitContent = '请输入内容';
    const inputBox = ref<HTMLElement | null>(null);

    function updateContent(value: string){
      if (inputBox.value && value !== inputBox.value.innerHTML) {
        inputBox.value.innerHTML = value;
      }
    };

    const requestFocus = () => {
      if (inputBox.value) {
        inputBox.value.focus();
      }
    };

    defineExpose({
      requestFocus
    })

    onMounted(() => {
      updateContent(props.content);
      if (inputBox.value) {
          inputBox.value.addEventListener("input", () => {
              if(inputBox.value != null){
                  //console.log(inputBox.value.innerHTML);
                  emit('update:content', inputBox.value.innerHTML);
              }
          });
      }
    });

    watch(() => props.content, (newValue: string) => {
      updateContent(newValue);
      if (inputBox.value) {
        //console.log(inputBox.value.innerHTML);
        inputBox.value.focus();
      }
    });
</script>

<template>
  <div class="rich-text-input">
    <div ref="inputBox" class="input-box" :placeholder="hitContent" contenteditable="true" spellcheck="false"></div>
  </div>
</template>

<style>
    .rich-text-input{
      width: 100%;
      height: 40vh;
    }
    .input-box:focus{
      border: #0084FF solid 0.2vh;
    }

    .input-box:empty:before {
        content: attr(placeholder);
        position: flex;
        color: lightgray;
        background-color: transparent;
    }

    .input-box {
      font-size: 17px;
      width: 96%;
      border-radius: 0.5vh;
      height: 40vh;
      overflow-y: auto;
      display: block;
      outline: none;
      border: #EBEDF0 solid 0.2vh;
      padding: 1vh;
      margin: 1vh;
    }

</style>