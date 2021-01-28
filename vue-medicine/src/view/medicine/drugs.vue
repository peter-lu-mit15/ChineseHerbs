<template>
  <div class="split-pane-page-wrapper">
    <split-pane v-model="offset" @on-moving="handleMoving">
      <div slot="right" class="pane right-pane">
        <Card>
          <p slot="title">
            <Icon type="paintbucket"></Icon>
            选择菜单
          </p>
          <Row type="flex" justify="center" class="countto-page-row" v-show="rows.count>0"
               v-for="(rows,index) in sel_list" :key="index">
            <Col span="12">
              <span>{{ rows.medicineName }}</span>
            </Col>
            <Col span="6" style="color:red;">
              <span>{{ rows.count }}</span>
              <span>g</span>
            </Col>
            <Col span="6" style="text-align:right;font-size:16px;">
              <Icon type="ios-close-circle" @click="del_item(rows)"/>
            </Col>
          </Row>
          <Row class="totalprice">
            <span>Total:${{totalprice}}</span>
          </Row>
          <Row>
            <Button type="success" @click="modal=true" long>Print</Button>
          </Row>
        </Card>
      </div>
      
      <div slot="left" class="pane left-pane">
        <div class="center_box">
         <div class="left_box">
           <Row v-for="item in A_list" :key="item">
             <Col span="24"  :class="select_A==item?'typea a_active':'typea'">
              <div @click="sel_type(item)">{{item}}</div>
             </Col>
           </Row>
         </div>
         <div class="right_box">
            <Row>
          <div class="search-con search-con-top2">
            <Select v-model="query.medicineNameCharacter" @on-change="change_china" class="search-col" style="width:120px;" >
              <Option v-for="item in chinalist" :value="item" :key="item">{{
                  item
                }}
              </Option>
            </Select>
            <Input style="width:150px;" @on-change="handleClear" clearable placeholder="输入关键字搜索" class="search-input"
                   v-model="query.medicineName"/>
            <Button @click="handleSearch" class="search-btn" type="primary">
              <Icon type="ios-search" />&nbsp;&nbsp;搜索
            </Button>
            <Button @click="cleardata" class="search-btn" style="margin-left:5px;" type="info">
              <Icon type="md-cube" />&nbsp;&nbsp;归原
            </Button>
          </div>
        </Row>
        <!-- main -->
        <Row :gutter="16">
          <Col span="6" v-for="(item,index) in datalist" :key="index" style="margin-bottom:30px;">
            <div class="box">
              <h4>{{ item.name }}</h4>
              <div>
                <img :src="item.image" alt="" srcset="">
              </div>
              <div class="btns">
                <Button type="info" :disabled="item.count<=0" @click="changecount('-',item)">-</Button>
                <InputNumber v-model="item.count" @on-change="numChange(item,$event)" style="width:80px;"/>
                <Button type="info" :disabled="item.medicineStock==item.count" @click="changecount('+',item)">+</Button>
              </div>
            </div>
          </Col>
        </Row>

        <Row style="text-align:center;margin-top:60px;">
          <Page :total="total"  :page-size="query.size" @on-change="changeSize" v-model="query" show-total /> 
        </Row>
         </div>
        </div>
      </div>
    </split-pane>

    <Modal
      v-model="modal"
      title="打印账单"
      :loading="loading"
      :footer-hide="true"
      @on-ok="printTest()">
      <div id="printMain" ref="print">
        <Row>
          <Col>
          <span class="date">date:{{currentTime}}</span>
          </Col>
        </Row>
        <Row>
          <Col span="12">
          <span class="date">Total:${{totalprice}}</span>
          </Col>
        </Row>
        <Row type="flex" justify="center" class="countto-page-row2" v-show="rows.count>0"
             v-for="(rows,index) in sel_list" :key="index">
          <Col span="12">
            <span>{{ rows.medicineName }}</span>
          </Col>
          <Col span="6">
            <span>{{ rows.count }}</span>
            <span>g</span>
          </Col>
          <Col span="6" style="color:red;">
            <span>$</span>
            <span>{{rows.medicinePrice}}</span>
          </Col>
        </Row>
      </div>
      <Row style="text-align:center;">
        <Button @click="modal=false" style="margin-right:20px;" >取消</Button>
        <Button v-print="printObj" type="success">打印</Button>
      </Row>
    </Modal>
  </div>
</template>

<script>
import SplitPane from '_c/split-pane'
import Icons from '_c/icons'
import { getProductList,getCharacterByPyin } from '@/api/customer'

export default {
  name: 'split_pane_page',
  components: {
    SplitPane,
    Icons
  },
  data () {
    return {
      searchKey:"",
      loading: true,
      modal: false,
      offset: 0.8,
      offsetVertical: '250px',
      printObj: {
        id: 'printMain',
        popTitle: '打印账单',
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>',
        endCallback:this.addnumber
      },
      datalist: [],
      sel_list:[],
      columns: [
        { title: '中药', key: 1 },
        { title: '西药', key: 2 },
      ],
      total:0,
      query:{
        pageNum:1,
        pageSize:12,
        pyName:"B",
        firstName:"",
        categoryId:"0",
        keyword:""
      },
      select_A:-1,
      A_list:[
        'A','B','C','D','E','F','G','H','I','J','K','L',
        'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
      ],
      chinalist:[],
      totalprice:0,
      timer:"",
      currentTime:new Date()
    }
  },
  created(){
    var _this = this; //声明一个变量指向Vue实例this，保证作用域一致
    this.timer = setInterval(function() {
      _this.currentTime = //修改数据date
        new Date().getFullYear() +
        "-" +
        (new Date().getMonth() + 1) +
        "-" +
        new Date().getDate() +
        " " +
        new Date().getHours() +
        ":" +
        new Date().getMinutes() +
        ": " +
        new Date().getSeconds();
    }, 1000);
  },
  beforeDestroy(){
    if (this.timer) {
      clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
    }
  },
  mounted () {
    this.initdata();

  },
  methods: {
    getdate(){
      var aData = new Date();
    console.log(aData) //Wed Aug 21 2019 10:00:58 GMT+0800 (中国标准时间)
    
    this.value =
      aData.getFullYear() + "-" + (aData.getMonth() + 1) + "-" + aData.getDate();
    console.log(this.value) //2019-8-20 
    },
    //批量记录库存数量
    addnumber(){
      var that=this;
      var arr=[];
      this.sel_list.forEach(item=>{
        if(item.count>0){
          var obj={
            medicineId:item.medicineId,
            stockCount:item.count
          };
          arr.push(obj);
        }
      })
      // batchTakeOffStock(arr).then(res=>{
      //   console.log(res);
      //   if(res.status==200){
      //     // that.modal=false;
      //     this.sel_list=[];
      //     this.totalprice=0;
      //     this.datalist.forEach(item=>{
      //       item.count=0;
      //     });
      //     this.modal=false;
      //     console.log("操作库存数据成功");
      //   }
      // });
    },
    changeSize(page){
      this.query.current=page;
      this.initdata();
    },
    initdata(){
      this.get_chainlist();
      getProductList(this.query).then(res=>{
        // console.log(res.data);
        var data=res.data.data.list;
        data.forEach(item=>{
          item.count=0;
        });
        this.datalist=data;
        this.total=res.data.data.total;
      });
    },
    handleMoving (e) {
      console.log(e.atMin, e.atMax)
    },
    //获取中文列表
    get_chainlist(){
      var params=new FormData();
      params.append("pyin",this.query.pyName);
      getCharacterByPyin(params).then(res=>{
        // console.log(res.data);
        var data=res.data.data;
        this.chinalist=data;
      });
    },
    sel_type(type){
      console.log(type);
      this.select_A=type;
      this.query.pyName=type;
      this.get_chainlist();
      this.initdata();
    },
    change_china(e){
      // console.log(this.query.medicineNameCharacter);
      this.initdata();
    },
    printTest () {

    },
    changecount (type, rows) {
      if (type === '-') {
        rows.count -= 1;
        var flag=false;
        this.sel_list.forEach(item=>{
          if(rows.medicineName==item.medicineName){
            // item.count-=1;
            flag=true;
          }
        })
        if(!flag){
          this.sel_list.push(rows);
        }
      } else {
        rows.count += 1
        if(this.sel_list.length<=0){
          this.sel_list.push(rows);
        }else{
          var flag=false;
          this.sel_list.forEach(item=>{
            if(rows.medicineName==item.medicineName){
              // item.count+=1;
              flag=true;
            }
          })
          if(!flag){
            this.sel_list.push(rows);
          }
        }
      }
      var totleprices=0;
      this.sel_list.forEach(item=>{
        if(item.count>0){
          totleprices+=item.medicinePrice*item.count;
        }
      });
      this.totalprice=totleprices;

    },
    del_item (rows) {
      rows.count = 0
    },
    numChange (item, e) {
      if (e >= 0) {
        if(e>item.medicineStock){
          setTimeout(() => {
          item.count = 0
        })
        }else{
          setTimeout(() => {
          item.count = e
        })
        }
        
      } else {
        setTimeout(() => {
          item.count = 0
        })
      }

    },
    cleardata(){
      this.sel_list=[];
          this.totalprice=0;
          this.datalist.forEach(item=>{
            item.count=0;
          });
          this.modal=false;
    },
    handleSearch () {
      this.initdata();
    },
    handleClear (e) {
      // if (e.target.value === '')
    },
  }
}
</script>

<style lang="less">
.center-middle {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.date {
  text-align: left;
  color: red;
  padding: 10px 5px;
}

.box {
  //background:#a8d3e8;
  background: #0f1423;
  height: 12rem;
  text-align: center;
  color: #fcfcfc;
  padding: 1rem;
  font-weight: bold;
  font-size: 16px;
  position: relative;

  img {
    width: 10rem;
    margin: 10px 0px;
    border-radius: 5px;
  }

  .btns {
    position: absolute;
    bottom: 15px;
    left: 50%;
    width: 100%;
    transform: translateX(-50%);

    input {
      text-align: center;
    }
  }
}

.box:hover {
  background: #313c5e;
}

.split-pane-page-wrapper {
  height: 600px;

  .pane {
    width: 100%;
    height: 100%;

    &.left-pane {
      background: rgb(255, 255, 255);
    }

    &.right-pane {
      background: #f5f7f9;
    }
  }

  .custom-trigger {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: #000000;
    position: absolute;
    .center-middle;
    box-shadow: 0 0 6px 0 rgba(28, 36, 56, 0.4);

    i.trigger-icon {
      .center-middle;
    }
  }
}
.center_box{
  overflow: hidden;
  .left_box{
    float: left;
    width: 7%;
    max-height:100vh;
    overflow-x: hidden;
    text-align: center;
    overflow-y: scroll;
    border-right: 1px solid #dcdee2;
    // padding-top:25px;
    line-height: 1.9rem;
    .typea{
      border:1px solid #ccc;
      font-weight: bold;
    }
    .typea:hover{
      background: #4fa0f4;
      color: #fff;
    }
  }
  .left_box::-webkit-scrollbar {
        display: none;
    }
  .right_box{
    float:right;
    width:93%;
    padding: 0.5rem;
  }
}
.a_active{
    background: #4fa0f4;
    color: #fff;
}
.totalprice{
  margin-top:60px;
  text-align:right;
  padding-right:5px;
  margin-bottom:8px;
  color:red;
  font-weight: bold;
}
</style>

<style>
.split-pane-page-wrapper {
  height: 100%;
}

.ivu-btn-info.disabled, .ivu-btn-info[disabled] {
  color: #000000;
  background-color: #ffffff;
  border-color: #d9dadb;
}

.countto-page-row {
  overflow: hidden;
  border: 1px solid #ccc;
  padding: 15px;
  margin-bottom: 20px;
}

.countto-page-row:hover {
  box-shadow: 0px 2px 2px 0px #ccc;
}

.countto-page-row2 {
  overflow: hidden;
  border-bottom: 1px dashed #ccc;
  padding: 15px;
}

.ios-close-circle {
  float: right;
}

.search-con-top2 {
  text-align: center;
  padding: 15px 10px !important;
}

</style>
