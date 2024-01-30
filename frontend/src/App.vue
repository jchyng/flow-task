<template>
  <div id="app">
    <form @submit.prevent>
      <div id="fixed-extensions">
        <span class="left-space">고정 확장자</span>
        <div v-for="(extension, idx) in fixedExtensions" :key="idx">
          <input
            type="checkbox"
            :id="extension.name"
            v-model="fixedExtensions[idx].isChecked"
          />
          <label :for="extension.name">{{ extension.name }}</label>
        </div>
      </div>

      <div id="custom-extensions">
        <span class="left-space">커스텀 확장자</span>
        <div>
          <div>
            <input
              type="text"
              placeholder="확장자 입력"
              v-model="inputExtension"
            />
            <button @click="addCustomExtension">+추가</button>
            <input
              type="checkbox"
              id="disableValidate"
              v-model="this.disableValidate"
            />
            <label for="disableValidate">프론트 검증 차단</label>
          </div>

          <div id="custom-extensions-area">
            <span id="custom-extensions-count"
              >{{ customExtensions.length }}/{{ maxCount }}</span>
            <div
              class="added-custom-extension"
              v-for="(extension, idx) in customExtensions"
              :key="idx"
            >
              <span>{{ extension }}</span>
              <button @click="removeCustomExtension(idx)">x</button>
            </div>
          </div>
          <div id="upload-file">
            <input type="file" @change="fileChange" />
            <button @click="uploadFile">파일 업로드</button>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  data() {
    return {
      initFixedExtensions: [],
      initCustomExtensions: [],
      fixedExtensions: [],
      customExtensions: [],
      inputExtension: "",
      maxCount: 200,
      uploadedFile: null,
      disableValidate: false
    };
  },
  methods: {
    addCustomExtension() {
      try{
        if(!this.disableValidate){
          this.validateExtensionLength(this.inputExtension);
          this.validateExtensionDuplicate(this.inputExtension);
          this.validateCustomExtensionsSize();
        }

        this.customExtensions.push(this.inputExtension.toLocaleLowerCase());
      } catch(e){
        alert(e.message);
      } finally {
        this.inputExtension = '';
      }
    },
    removeCustomExtension(idx) {
      this.customExtensions.splice(idx, 1);
    },
    fileChange(event) {
      const inputFile = event.target;

      if (inputFile.files.length <= 0) return;

      try{
        if(!this.disableValidate){
          this.validateFileExtensionRestricted(inputFile.files[0].name);
          this.validateFileLength(inputFile.files[0].name)
          this.validateFileNameInSpecialCharacter(inputFile.files[0].name);
        }

        this.uploadedFile = inputFile.files[0];
      } catch(e){
        alert(e.message);
        this.uploadedFile = null;
        inputFile.value = "";
      }

    },
    uploadFile() {
      if (this.uploadedFile === null) {
        alert("파일을 업로드 해주세요.");
        return;
      }

      var dto = {
        fixedExtensionDtos: this.dirtyCheckFixedExtension(),
        addedCustomExtensionDtos: this.getAddedCustomExtension(),
        removedCustomExtensionDtos: this.getRemovedCustomExtension(),
      };

      const formData = new FormData();
      formData.append("file", this.uploadedFile);
      formData.append("extensionDto", new Blob([JSON.stringify(dto), { type: "application/json" }]));

      axios
        .post("http://3.37.22.176:8081", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          alert(response.data);
          window.location.reload();
        })
        .catch((error) => {
          if (error.response.status === 400) {
            alert(error.response.data);
          }
        });
    },
    dirtyCheckFixedExtension() {
      return this.fixedExtensions.filter((item) => this.initFixedExtensions.some(
        (initItem) => initItem.id === item.id && initItem.isChecked !== item.isChecked)
      );
    },
    getAddedCustomExtension() {
      return this.customExtensions.filter(
        (item) => !this.initCustomExtensions.includes(item)
      );
    },
    getRemovedCustomExtension() {
      return this.initCustomExtensions.filter(
        (initItem) => !this.customExtensions.includes(initItem)
      );
    },
    getFileExtension(filename) {
      const dotIndex = filename.lastIndexOf(".");
      return dotIndex !== -1 ? filename.slice(dotIndex + 1) : "";
    },
    getFileNameWithoutExtension(filename) {
      const dotIndex = filename.lastIndexOf(".");
      return dotIndex !== -1 ? filename.slice(0, dotIndex) : filename;
    },
    getCheckedFixedExtensionNames() {
      return this.fixedExtensions
        .filter((extension) => extension.isChecked)
        .map((extension) => extension.name);
    },
    validateExtensionLength(extension){
      if (extension.length < 1 || extension.length > 20) {
        throw new Error("확장자는 1~20자까지 허용됩니다.");
      }
    },
    validateExtensionDuplicate(extension) {
      if(this.customExtensions.includes(extension) || this.getCheckedFixedExtensionNames().includes(extension)){
          throw new Error("확장자는 중복 입력할 수 없습니다.");
      }
    },
    validateCustomExtensionsSize(){
      if (this.customExtensions.length >= this.maxCount) {
        throw new Error("확장자는 최대 " + this.maxCount + "까지만 입력하실 수 있습니다.");
      }
    },
    validateFileExtensionRestricted(filename) {
      var extension = this.getFileExtension(filename);
      var checkedFixedExtensions = this.getCheckedFixedExtensionNames();

      if(this.customExtensions.indexOf(extension) !== -1 && checkedFixedExtensions.indexOf(extension) !== -1){
        throw new Error(extension + '는 사용이 제한된 확장자 입니다.');
      }
    },
    validateFileLength(filename){
      const fineNameWithoutExtension = this.getFileNameWithoutExtension(filename);
      
      if(fineNameWithoutExtension.length > 260){
        throw new Error("파일 이름은 260자를 초과할 수 없습니다.");
      }
    },
    validateFileNameInSpecialCharacter(filename){
      const allowedPattern = /^[가-힣a-z0-9_\s-]+$/i;
      const fineNameWithoutExtension = this.getFileNameWithoutExtension(filename);

      if (!allowedPattern.test(fineNameWithoutExtension)) {
        throw new Error("파일 이름은 '-', '_' 이외의 특수 문자를 사용할 수 없습니다.");
      }
    }
  },
  mounted() {
    axios
      .get("http://3.37.22.176:8081")
      .then((response) => {
        this.fixedExtensions = response.data.fixedExtensionDtos;
        this.initFixedExtensions = JSON.parse(
          JSON.stringify(this.fixedExtensions)
        );
        this.customExtensions = response.data.customExtensionDtos;
        this.initCustomExtensions = JSON.parse(
          JSON.stringify(this.customExtensions)
        );
      })
      .catch((error) => {
        console.log("확장자 조회 실패: ", error);
      });
  },
};
</script>

<style>
@import "./assets/css/style.css";
</style>
