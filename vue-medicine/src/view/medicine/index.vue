
<template>
  <div class="layout">
    <Layout :style="{ minHeight: '100vh' }">
      
      <Sider
        collapsible
        :collapsed-width="78"
        :width="78"
        v-model="isCollapsed"
      >
          <div class="left_box">
            <Row v-for="item in A_list" :key="item">
              <Col span="24"  :class="select_A==item?'typea a_active':'typea'">
                <div @click="sel_Pyin(item)">{{item}}</div>
              </Col>
            </Row>
          </div>
      </Sider>
      <Sider
        collapsible
        :collapsed-width="0"
        :width="120"
        hide-trigger
        v-model="isCollapsed"
      >
          <div class="left_box">
            <Row v-for="(item,index) in chinalist" :key="index">
              <Col span="24"  :class="select_C==item?'typea a_active':'typea'">
                <div @click="sel_China(item)">{{item}}</div>
              </Col>
            </Row>
          </div>
      </Sider>
      <Layout>
        <Header
          :style="{
            background: '#fff',
            boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)',
          }"
        >
          <Row>
          <div class="search-con search-con-top2">
            <Input style="width:150px;"  clearable placeholder="输入关键字搜索" class="search-input"
                   v-model="query.keyword"/>
            <Button @click="handleSearch" class="search-btn" style="margin-left:5px;" type="primary">
              <Icon type="ios-search" />&nbsp;&nbsp;搜索
            </Button>
            <Button @click="cleardata" class="search-btn" style="margin-left:5px;" type="info">
              <Icon type="md-cube" />&nbsp;&nbsp;归原
            </Button>
            <!-- <Button @click="show_cart" class="search-btn" style="margin-left:5px;" type="info">
              <Icon type="md-cube" />&nbsp;&nbsp;我的配方
            </Button> -->
          </div>
        </Row>
        </Header>
        <Content :style="{ padding: '0 16px 16px', margin: '16px 0',width:'68%' }">
          <Card>
            <div style="height: 600px">
              <!-- main -->
              <Row :gutter="16">
                <Col
                  :xs="24" :sm="8" :md="6" 
                  v-for="(item, index) in datalist"
                  :key="index"
                  style="margin-bottom: 30px"
                >
                  <div class="box">
                    <h4>{{ item.name }}</h4>
                    <div>
                      <img :src="item.image" alt="" srcset="" />
                      <div class="product_detail">{{get_detail(item.detail)}}</div>
                    </div>
                    <div>
                      <InputNumber 
                        :formatter="value => `${value}g`" 
                        :parser="value => value.replace('g', '')"
                        size="small" v-model="item.count" 
                        :max="item.stock" :min="0"
                        @on-change="numChange(item,$event)" 
                        style="width:80px;"
                      />
                      <Select v-model="item.typeid" size="small"   class="search-col" style="width:80px;" >
                        <Option   v-for="(it,index2) in item.variantList" :value="index2" :key="index2">{{
                            it.name
                          }}
                        </Option>
                      </Select>
                    </div>
                    <div>
                      <InputNumber 
                        :formatter="value => `配方号:${value}`" 
                        :parser="value => value.replace('配方号:', '')"
                        size="small" 
                        v-model="item.formula" 
                        :max="cartcount" :min="1"
                        @on-change="numChange2(item,$event)" 
                        style="width:80px;"
                      />
                      <Button size="small" @click="addcart(item)" type="info" style="width:80px;">add</Button>
                    </div>
                  </div>
                </Col>
              </Row>
              <!-- 分页组件 -->
              <Row style="text-align:center;position: absolute;bottom:10px;left:50%;transform: translateX(-50%);">
                <Page :total="query.total"  :page-size="query.pageSize" @on-change="changeSize" v-model="query" show-total /> 
              </Row>
            </div>
          </Card>
        </Content>
      </Layout>
    </Layout>

    <Drawer
      title="我的配方"
      v-model="formModel.opened"
      width="28%"
      :closable="false"
      :mask-closable="false"
      :mask="false"
      :styles="styles"
    >
      <!--添加修改弹出窗口-->
      <Form :model="formModel.fields" ref="formNotice" :rules="formModel.rules" label-position="top">
        
       <Card v-for="(item,index) in cart.data" v-show="item.length>0">
        <div style="font-weight:bold;color:red;overflow:hidden;">
          <span>配方{{(index+1)}}</span>
          <span>----</span>
          <span>合计：${{get_totalprice(item)}}</span>
        </div>
        <dz-table
          :totalCount="item.length"
          :pageSize="cart.query.pageSize"
          :currentPage="cart.query.pageNum"
          :showPage="false"
        >
          <!-- *******************************信息显示********************************  -->
          <Table
            slot="table"
            ref="tables"
            :border="false"
            size="small"
            :data="item"
            :columns="cart.columns"
          >
          <template slot-scope="{ row }" slot="product_price">
              <span style="color:red;">${{ row.totalPrice}}</span>
          </template>
          <template slot-scope="{ row }" slot="action">
            <Tooltip placement="top" content="删除" :delay="1000" :transfer="true">
              <Button @click="handleDelete(row,index)"  type="error" size="small" shape="circle" icon="md-trash"></Button>
            </Tooltip>
          </template>
          </Table>
        </dz-table>
      </Card>
      <Row>
        <div style="overflow:hidden;">
          <Checkbox v-model="single" @on-change="change_zhu" :disabled="single_disable">煮药</Checkbox>
          <span class="allprice">总金额：${{all_prilce}}</span>
        </div>
      </Row>
      <Row :gutter="16">
          <Col span="24">
            <FormItem label="备注信息" >
              <Input   v-model="order_remarks" placeholde------------------------------------r="备注信息" />
            </FormItem>
          </Col>
        </Row>
        
      </Form>
      <Row style="text-align:center;position: absolute;bottom:10px;left:50%;transform: translateX(-50%);">
        <Button icon="md-checkmark-circle" type="primary" @click="handleSubmitUser" v-show="formModel.mode!='show'">发 送</Button>
        <!-- <Button style="margin-left: 8px" icon="md-close" @click="formModel.opened = false">取 消</Button> -->
      </Row>
    </Drawer>
  </div>
</template>
<script>
import DzTable from "_c/tables/dz-table.vue";
import { 
  getProductList, 
  getCharacterByPyin,
  addCartList,
  boiledCount,
  sendEmailForOrder,
  boiledList
} from "@/api/customer";

export default {
  components: {
    DzTable,
  },
  data() {
    return {
      formModel: {
        opened: true,
        title: "创建订单",
        mode: "create",
        selection: [],
        fields: {
          orderNo: "",
          orderStatusName: "",
          receiverAddress: "",
          receiverMobile: "",
          id: 0,
          receiverName: "",
          totalPrice: "",
          userId: "",
          orderItemVOList: []
        },

        rules: {
          username: [{ type: "string",required: true, message: "请输入账号" }],
          email: [{ type: "string",required: true, message: "请输入用户邮箱" }],
        },
      },
      cart:{
          data:[],
          query: {
            pageNum: 1,
            pageSize: 50,
            totalCount: 0,
          },
          columns: [
            { title: "序号", type: "index", width: 50 },
            { title: "药材名称", key: "name" },
            { title: "单价", key: "price" },
            { title: "数量", key: "count" },
            { title: "类型", key: "typename" },
            { title: "总价", key: "totalPrice",slot:"product_price"  },
            {
              title: "操作",
              align: "center",
              className: "table-command-column",
              slot: "action",
            },
          ],
      },
      styles: {
        height: "calc(100% - 55px)",
        overflow: "auto",
        paddingBottom: "53px",
        position: "static",
      },
      isCollapsed: false,
      
      datalist: [],
      sel_list: [],
      typelist: [
        { title: "片状", key: 1 },
        { title: "块状", key: 2 },
        { title: "粉末", key: 3 },
      ],
      query: {
        total: 0,
        pageNum: 1,
        pageSize: 8,
        pyName: "C",
        firstName: "",
        categoryId: "0",
        keyword: "",
      },
      select_A: "",
      select_C: "",
      A_list:[
        'A','B','C','D','E','F','G','H','I','J','K','L',
        'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
      ],
      chinalist: [],
      totalprice: 0,
      all_prilce:0,
      single:false,
      single_disable:true,
      order_remarks:"",
      zhu_price:{
        min:1,
        max:1
      },
      userId:'',
      cartcount:0,
      boiledMin:0,
      boiledListdata:[],

    };
  },
  computed: {
    menuitemClasses: function () {
      return [
        "menu-item",
        this.isCollapsed ? "rotate-icon" : "",
        // this.isCollapsed ? 'collapsed-menu' : ''
      ];
    },
  },
  mounted(){
    
    this.initdata();
    //获取配方数
    this.get_boiledCount();
    //煮药价格收费列表
    this.get_boiledList();
  },
  methods: {
    get_boiledList(){
      var params={pageNum:1,pageSize:50};
      boiledList(params).then(res=>{
        if(res.data.status==200){
          var data=res.data.data;
          this.boiledListdata=data.list;
        }
      });
    },
    get_boiledCount(){
      var that=this;
      boiledCount().then(res=>{
        if(res.data.status==200){
          var data=res.data.data;
          that.cartcount=data.boidedMax;
          that.boiledMin=data.boidedMin;
          //初始化购物车
          that.cart.data=[];
          for(var i=1;i<=10;i++){
            that.cart.data.push([]);
          }
        }
      });
    },
    handleSubmitUser(){
      var param_list=[];
      for(var i=0;i<this.cart.data.length;i++){
        var arr=this.cart.data[i];
        if(arr.length>0){
          for(var j=0;j<arr.length;j++){
            var item=arr[j];
            var obj={
              productId:item.id,
              count:item.count,
              handle:item.variantList[item.typeid].id,
              // handle:item.typeid,
              formula:(i+1),
              remarks:this.order_remarks,
              userId:this.userId
            };
            param_list.push(obj);
          }
        }
      }
      if(param_list.length<=0){
        this.$Message.warning("请先添加药材");
        return;
      }
      addCartList(param_list).then(res=>{
        if(res.data.status==200){
          this.$Message.success("提交成功");
          this.cleardata();
          var orderNo=res.data.data;
          sendEmailForOrder({orderNo:orderNo}).then(result=>{
          });
        }else{
          this.$Message.success("系统错误，添加失败");
        }
      });
    },
    //检测登录
    is_havalogin(){
      if(this.$store.state.userId=='' || this.$store.state.userId==undefined || this.$store.state.userId==null){
          this.$Message.warning("请重新登录");
          this.$router.push({name:"customerLogin"});
      }else{
          this.userId=this.$store.state.userId;
      }
    },
    get_detail(txt){
      var str="暂无介绍";
      if(txt!=null || txt==""){
        str=txt;
      }
      return str;
    },
    changeSize(page){
      this.query.pageNum=page;
      this.initdata();
    },
    initdata(){
      this.is_havalogin();

      this.select_A=this.query.pyName;
      this.get_chainlist();
      getProductList(this.query).then(res=>{
        //检测登录
        if(res.data.status==10007){
          this.$Message.warning("请重新登录");
          this.$router.push({name:"customerLogin"});
        }          
        var data=res.data.data.list;
        data.forEach(item=>{
          item.count=0;
          item.typeid=-1;
          item.formula=1;
        });
        console.log(data);
        this.datalist=data;
        this.query.total=res.data.data.total;
      });
    },
    //获取中文列表
    get_chainlist(){
      var params=new FormData();
      params.append("pyin",this.query.pyName);
      getCharacterByPyin(params).then(res=>{
        var data=res.data.data;
        this.chinalist=data;
      });
    },
    sel_Pyin(type){
      this.select_A=type;
      this.query.pyName=type;
      this.query.firstName="";
      // this.get_chainlist();
      this.initdata();
    },
    sel_China(type){
      this.select_C=type;
      this.query.firstName=type;
      this.initdata();
    },
    //选择药材数量
    numChange (item, e) {
      item.count = e
    },
    //选择方案
    numChange2 (item, e) {
      item.formula = e
    },
    //添加药材到购物车
    addcart(item){
      if(item.count<1){
        this.$Message.warning("请选择数量");
        return;
      }
      if(item.typeid==undefined) item.typeid=-1;
      if(item.typeid<0){
        this.$Message.warning("请选择处理类型");
        return;
      }
      // var obj=Object.assign({},item);
      var obj={...item};
      var flag=false;
      //检测之前的数据
      this.cart.data[item.formula-1].forEach(item=>{
        if(obj.id==item.id ){//且类型一致
          flag=true;
          //检测类型
          if(item.typeid!=obj.typeid){
            this.$Message.error("此方案中已存在此药材，类型不一致，请添加到其他配方中");
            return;
          }else{
            item.count+=obj.count;
            item.totalPrice=(item.price*item.count)+(item.variantList[item.typeid].price*item.count);
            this.get_allprice();
            this.$Message.success("添加成功");
            return;
          }
        }
      });
      if(!flag){
        //（单价*数量）+（数量*每个处理价格）
        obj.totalPrice=(obj.price*obj.count)+(obj.variantList[obj.typeid].price*obj.count);
        obj.typename=obj.variantList[obj.typeid].name;
        this.cart.data[item.formula-1].push(obj);

        this.get_allprice();
        this.$Message.success("添加成功");
      }

      //重置数据
      item.count=0;
      item.formula=1;
      item.typeid=-1;
      
    },
    handleDelete(rows,index) {
      if (!rows) {
        this.$Message.warning("请选择至少一条数据");
        return;
      }
      var del_id=-1;
      for(var i=0;i<this.cart.data[index].length;i++){
        if(this.cart.data[index].id==rows.id){
          del_id=i;
          break;
        }
      }
      //删除
      this.cart.data[index].splice(del_id,1);
      this.get_allprice();
      this.$Message.success("删除成功");
    },
    get_allprice(){
      var total=0;
      var count=0;
      for(var i=0;i<this.cart.data.length;i++){
        var item=this.cart.data[i];
        if(item.length>0){
            count+=1;
          for(var j=0;j<item.length;j++){
            var it=item[j];
            total+=it.totalPrice;
          }
        }
      }

      this.single_disable=count<this.boiledMin?true:false;
      var zhu_prices=0;//煮药总价
      var boiledPrice=0;//煮药单价
      //根据数量获取煮药的价格
      for(var i=0;i<this.boiledListdata.length;i++){
        if(count==this.boiledListdata[i].count){
          boiledPrice=this.boiledListdata[i].price;
          break;
        }
      }

      //true:开启煮药
      if(this.single){
        zhu_prices=count*boiledPrice;
        this.all_prilce=(total+zhu_prices);
      }else{
        this.all_prilce=total;
      }
    },
    get_totalprice(arr){
      var total=0;
      for(var i=0;i<arr.length;i++){
        total+=arr[i].totalPrice;
      }
      return total;
    },
    show_cart(){
      this.get_allprice();
      this.formModel.opened=true;
    },
    change_zhu(){
      this.get_allprice();
    },
    cleardata(){
      //初始化购物车
      this.get_boiledCount();
      this.query.firstName="";
      this.query.keyword="";
      this.query.keyword="";
      this.order_remarks="";
      this.all_prilce=0;
      this.sel_Pyin("A");
    },
    handleSearch () {
      this.initdata();
    },
  }
};
</script>
<style lang="less" scoped>
.layout-con {
  height: 100%;
  width: 100%;
}
.menu-item span {
  display: inline-block;
  overflow: hidden;
  width: 69px;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: bottom;
  transition: width 0.2s ease 0.2s;
}
.menu-item i {
  transform: translateX(0px);
  transition: font-size 0.2s ease, transform 0.2s ease;
  vertical-align: middle;
  font-size: 16px;
}
.collapsed-menu span {
  width: 0px;
  transition: width 0.2s ease;
}
.collapsed-menu i {
  transform: translateX(5px);
  transition: font-size 0.2s ease 0.2s, transform 0.2s ease 0.2s;
  vertical-align: middle;
  font-size: 22px;
}
.my_menu {
  text-align: center;
}
.box {
  min-width: 150px;
  background: #0f1423;
  text-align: center;
  color: #fcfcfc;
  padding: 0.5rem;
  font-weight: bold;
  font-size: 16px;
  position: relative;

  img {
    width: 90%;
    max-height:82px ;
    margin: 10px 0px;
    border-radius: 5px;
  }

}

.box:hover {
  background: #313c5e;
}
.ivu-layout-sider{
    // max-height: 100vh !important;
    // overflow-x: hidden !important;
    // overflow-y: overlay !important;
}
.ivu-layout-sider-trigger{
  width: 110px !important;
}
.search-con-top2 {
  text-align: center;
}



.left_box{
    float: left;
    width: 100%;
    max-height:100vh;
    overflow-x: hidden;
    text-align: center;
    overflow-y: scroll;
    // border-right: 1px solid #dcdee2;
    // padding-top:25px;
    line-height: 1.9rem;
    .typea{
      // border:1px solid #ccc;
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
.allprice{
  float: right;
  margin: 5px;
  font-weight: bold;
  color: red;
}
.product_detail{
  font-size: 12px;
  font-weight: normal;
  min-height: 1.2rem;
  max-height: 1.2rem;
}
</style>