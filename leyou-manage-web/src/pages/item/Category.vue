<template>
  <v-card>
      <v-flex xs12 sm10>
        <v-tree url="/item/category/list"
                :isEdit="isEdit"
                @handleAdd="handleAdd"
                @handleEdit="handleEdit"
                @handleDelete="handleDelete"
                @handleClick="handleClick"
        />

       <!-- :treeData="treeData"-->
      </v-flex>
  </v-card>
</template>

<script>

  import {treeData} from "../../mockDB.js";

  export default {
    name: "category",
    data() {
      return {
        isEdit:true,
        treeData
      }
    },
    methods: {
      handleAdd(node) {
        /*console.log("add .... ");
        console.log(node);*/
        this.$http({
          method: 'post',
          url: '/item/category',
          data: this.$qs.stringify({
            name:node.name,
            isParent:node.isParent,
            parentId:node.parentId,
            sort:node.sort
          })  //params的json数据 转换成 ？name=a&age=11
        })

      },
      handleEdit(id, name) {
        console.log("edit... id: " + id + ", name: " + name)
        this.$http({
          method: 'put',
          url: '/item/category',
          data: this.$qs.stringify({
            id:id,
            name:name
          })  //params的json数据 转换成 ？name=a&age=11
        })

      },
      handleDelete(id) {
        console.log("delete ... " + id)
        this.$http.delete("/item/category/" + id)
      },
      handleClick(node) {
        console.log(node)
      }
    }
  };
</script>

<style scoped>

</style>
