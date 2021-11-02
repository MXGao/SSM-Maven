## 注意：
1. 配置内部资源解析器时，前缀配置："/"；
2. ModelAndView进行页面跳转时，重定向无法用${key}获取到数据,只能用${param.get(key)},但获取不到对象；
   所以，尽量不用视图重定向；
3. jsp中引入的外部js和css文件都使用绝对路径;
4. 通过jsp发送ajax请求，只能获取到ModelAndView中的字符串数据，不能获取到对象或集合等数据；
5. 存入redis中的实体类需要进行序列化（实现Serializable接口）
6. 在前台jsp中获取后台存入session中的map中的值时，需要加上value（如：${cartItem.value.product.pname}）

## 注册（激活）
1. 首先，在用户名文本框写完用户名后，可以向后台发送一次ajax请求，来判断用户名是否已经存在；这样就可以不用全部写完再发送；
2. 用户信息填写完毕后，在register.jsp页面将数据提交到后台；
3. 在controller层，通过request获取到数据集合，通过BeanUtils工具类，将数据封装进User对象中；（日期格式的数据需要通过类型转换器转换）
4. 再通过CommonsUtils工具类随机生成uid，设置到User对象中；
5. 将user对象传递到service和dao层，判断user是否存在：已存在，直接返回false；不存在，则设置激活码和激活状态，通过MailUtils向邮箱发送激活邮件
6. controller层接收返回来的boolean类型的值，判断是否注册成功，跳转到相应的页面。
# 激活
1. 注册成功后，点击邮箱中中收到的激活邮件链接(携带激活码发送至后台)，进行激活
2. controller层收到激活码后，传递到service层；dao层通过激活码查询user信息，有就可以进行激活(激活状态变为1)，没有返回false；
3. controller接收到返回的boolean的值后，判断是否激活成功，跳转至相应的页面

## 登录
1. login.jsp页面将用户名和密码发送至后台进行登录
2. 后台接收到参数后，根据用户名和密码进行查询用户信息，判断用户是否存在；
3. 存在，等登陆成功，将user对象存储在session域中，并跳转至主页面；
4. 而且header.jsp中信息需要根据是否登录，来进行展示；根据session中user是否存在，来分别进行展示，通过<c:if>标签

## 退出
1. 将session域中user移除就表示退出了（或清空session域）

## 产品分类名展示
1. 需要在登录成功后，跳转到index.jsp中就将分页条在header.jsp上展示出来，根据是否有user来判断是否展示；
2. 在页面加载完毕后，向后台发送ajax请求，查询数据库，获取category信息，从而展示；
3. 注意：第一次查询数据库，将信息储存到redis缓存中，后面查询就可以直接从redis中查询，减轻数据库的压力

## 不同类型的产品分页展示
1. 分析数据库的数据表，同一种类型的产品cid相同，可以根据cid来查询不同类型的产品
2. 分页展示需要PageBean对象，存储着分页展示的信息，同时将根据同一cid查询出来的商品集合也作为属性设置到PageBean中
3. 需要传入后台的参数有：cid，currentPage和pageSize（不传入，则默认指定），将三个参数传入service层进行查询封装PageBean对象；
4. 在service层对PageBean进行属性封装，其中product的list集合需要根据cid和分页标准查询出对应数量；
5. 将PageBean反馈到product_list.jsp页面展示，注意分页的展示，每个页码都需要将cid和currentPage传入到后台进行查询

## 产品信息展示
1. 点击商品图片，或名字，发送请求到后台，传递商品的pid
2. 后台接收到pid后，根据pid进行查询数据库，查询到对应商品，封装对象后，在product_info.jsp页面进行展示；

## 添加购物车（添加前需要判断是否登录）
1. 在product_info.jsp中点击加入购物车后，后台会将需要创建单种商品的购物车对象CartItem，和总购物车Cart；
2. 每次，将信息存到CartItem中，包括商品数量、商品对象、总价钱；
3. Cart是用来获取所有添加的商品，里面需要属性：以键值对储存CartItem的map集合(pid为键)，以及总价钱；
4. 然后将Cart存到session中，转发到cart.jsp页面；
5. 在cart.jsp页面获取数据，遍历数据
6. 注意：每次点击加入购物车，都需要传递pid，所以map集合需要定义在方法外，如果在方法内创建，每次都会是新的集合，不能累加pid

## 提交订单
1. 点击cart.jsp中的提交订单，跳转至后台，利用存在session域中的购物车，封装Order订单以及OrderItem订单项
2. session域中cart的cartItem的map集合与orderItem一一对应，需要把map集合中的元素一一取出来，分别封装到orderItem中，再把orderItem添加到order的list集合中

## 我的订单
1. 点击我的订单，需要展示属于该用户的所有订单，每个订单的订单项集合又不一样，而且无法直接从数据库中查询，只能单独进行封装
2. 通过查询数据库，将每一个订单项查询出来，分别封装到订单项集合中，再把每个订单项集合设置到对应的订单对象中
3. 注意：这里查询订单项时，只需要封装需要的数据，多余的数据可以不用查询出来进行封装。