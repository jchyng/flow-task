<template>
  <div id="app">
    <form @submit.prevent>
        <div id="fixed-extensions">
            <span class="left-space">고정 확장자</span>
            <div v-for="(extension, idx) in fixedExtensions" :key="idx">
              <input type="checkbox" :id="extension.name" v-model="fixedExtensions[idx].isChecked">
              <label :for="extension.name">{{extension.name}}</label>
            </div>
        </div>

        <div id="custom-extensions">
          <span class="left-space">커스텀 확장자</span>
          <div>
            <div>
                <input type="text" placeholder="확장자 입력" v-model="inputExtension">
                <button @click="addCustomExtension">+추가</button>
            </div>

            <div id="custom-extensions-area">
                <span id="custom-extensions-count">{{ customExtensions.length }}/{{ maxCount }}</span>
                <div class="added-custom-extension" v-for="(extension, idx) in customExtensions" :key="idx">
                    <span>{{ extension }}</span>
                    <button @click="removeCustomExtension(idx)">x</button>
                </div>
            </div>
            <div id="upload-file">
              <input type="file" @change="fileChange"/>
              <button @click="uploadFile">파일 업로드</button>
            </div>
          </div>
        </div>
      </form>
    </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      fixedExtensions: [],
      customExtensions: [],
      inputExtension: "",
      maxCount: 200,
      uploadedFile: null,
    }
  },
  methods: {
    addCustomExtension(){
      if(this.validateInputCustomExtensions()){
        this.customExtensions.push(this.inputExtension);
        this.inputExtension = "";
      }
    },
    removeCustomExtension(idx){
      this.customExtensions.splice(idx, 1);
    },
    fileChange(event){
      const fileInput = event.target;

      if (fileInput.files.length > 0) {
        this.uploadedFile = fileInput.files[0];
      } else {
        this.uploadedFile = null;
        fileInput.value = '';
      }
    },
    uploadFile(){
      var uploadedFile = this.uploadedFile;
      var dto = {
        fixedExtensionDtos : this.fixedExtensions,
        customExtensionDtos: this.customExtensions,
      }

      if (uploadedFile === null || !this.validateFileExtension(uploadedFile.name)) {
        alert('제한된 확장자를 제외한 파일을 업로드 해주세요.')
        return;
      }

      const formData = new FormData();
      formData.append('file', uploadedFile);
      formData.append('extensionDto', new Blob([
        JSON.stringify(dto), {
          type: "application/json"
        }
      ]));

      axios.post('http://3.37.22.176:8080', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      .then(() => {
        alert("파일이 정상적으로 업로드 되었습니다.");
        window.location.reload(); 
      })
      .catch(error => {
        if(error.response.status === 400)
        console.error('파일 업로드 실패: 업로드가 제한된 확장자 입니다.');
      });
      
    },
    getFileExtension(filename) {
      const dotIndex = filename.lastIndexOf(".");
      return dotIndex !== -1 ? filename.slice(dotIndex + 1) : "";
    },
    getCheckedFixedExtensionNames() {
    return this.fixedExtensions
      .filter(extension => extension.isChecked)
      .map(extension => extension.name);
    },
    inputCustomExtensionIsDuplicate(){
      var isIncludeInCustomExtensions = this.customExtensions.includes(this.inputExtension);
      var isIncludeInFixedExtensions = this.getCheckedFixedExtensionNames().includes(this.inputExtension)

      return isIncludeInCustomExtensions || isIncludeInFixedExtensions;
    },
    validateInputCustomExtensions(){
      if(this.inputExtension === ""){
        alert("확장자를 입력해주세요");
        return false;
      }
      if(this.inputCustomExtensionIsDuplicate()) {
        alert("확장자는 중복 입력할 수 없습니다.");
        this.inputExtension = "";
        return false;
      }
      if(this.customExtensionDtos.length >= this.maxCount){
        alert("확장자는 최대 " + this.maxCount + "까지만 입력하실 수 있습니다.");
        this.inputExtension = "";
        return false;
      }

      return true;
    },
    validateFileExtension(filename){
      var extension = this.getFileExtension(filename);
      var checkedFixedExtensions = this.getCheckedFixedExtensionNames();

      if(this.customExtensions.indexOf(extension) !== -1 || checkedFixedExtensions.indexOf(extension) !== -1){
        alert(extension + '확장자는 업로드 할 수 없습니다.');
        return false;
      }
      return true;
    }
  },
  mounted(){
    axios.get('http://3.37.22.176:8080')
        .then(response => {
          this.fixedExtensions = response.data.fixedExtensionDtos;
          this.customExtensions = response.data.customExtensionDtos;
        })
        .catch(error => {
          console.error('확장자 조회 실패: ', error);
        })
  }
}
</script>

<style>
  @import "./assets/css/style.css"
</style>