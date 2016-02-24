var tooltip=function(){
	
	var id = 'tt';
	var top = 3;
	var left = 3;
	var position='absolute';
	var maxh = 400;
	var maxw = 400;
	var speed = 10;
	var timer = 2;
	var endalpha = 95;
	var alpha = 0;
	var tt,t,c,b,h;
	var ie = document.all ? true : false;
	
	return {
		show:function(v){
			document.onclick= this.pos;
			left=3;
			var w=maxw+'px';
			if(tt == null){
				tt = document.createElement('div');
				tt.setAttribute('id',id);
				t = document.createElement('div');
				t.setAttribute('id',id + 'top');
				c = document.createElement('div');
				c.setAttribute('id',id + 'cont');
				close = document.createElement('input');
				close.setAttribute('id',id + 'close');
				close.setAttribute('type','button');
				close.onclick=function(){tooltip.hide();};
				close.value='X';
				b = document.createElement('div');
				b.setAttribute('id',id + 'bot');
				tt.appendChild(t);
				tt.appendChild(c);
				tt.appendChild(b);
				document.body.appendChild(tt);
				tt.style.opacity = 0;
				tt.style.filter = 'alpha(opacity=0)';
			
			}
			tt.style.display = 'block';
			c.innerHTML = "<STRONG>MORE INFO</STRONG><div style=\"float: right;\"><input type=\"button\" onclick=\"tooltip.hide();\" value=\" X \" /></div><BR><div style=\"color:#000000;background-color:#BEBEBE;height:95%;width:100%;overflow:scroll;\" >"+v+"</div>";
			c.style.height=h;
//			alert("TT height: "+parseInt(c.offsetHeight));
			if(parseInt(c.offsetHeight)>maxh)
				c.style.height=maxh+"px";
			tt.style.width = w;
			if(!w && ie){
				t.style.display = 'none';
				b.style.display = 'none';
				close.style.display = 'none';
				tt.style.width = w;//tt.offsetWidth;
				t.style.display = 'block';
				b.style.display = 'block';
				close.style.display = 'block';
			}
			
			clearInterval(tt.timer);
			tt.timer = setInterval(function(){tooltip.fade(1)},timer);
			temp =0;
		},
		
		pos:function(e){
					var u = ie ? event.clientY + document.documentElement.scrollTop : e.pageY;
					var l = ie ? event.clientX + document.documentElement.scrollLeft : e.pageX;
					if(left==3)
					{
					top = (u - maxh - 16);
					left =(l + left) ;

					tt.style.top =  top+"px";//(top - h) + 'px';
					tt.style.left = left+"px";//(l + left) + 'px';
					}
		},
		fade:function(d){
			var a = alpha;
			if((a != endalpha && d == 1) || (a != 0 && d == -1)){
				var i = speed;
				if(endalpha - a < speed && d == 1){
					i = endalpha - a;
				}else if(alpha < speed && d == -1){
					i = a;
				}
				alpha = a + (i * d);
				tt.style.opacity = alpha * .01;
				tt.style.filter = 'alpha(opacity=' + alpha + ')';
			}else{
				clearInterval(tt.timer);
				if(d == -1){tt.style.display = 'none'}
			}
			
		},
		hide:function(){
			clearInterval(tt.timer);
			tt.timer = setInterval(function(){tooltip.fade(-1)},timer);
		}
	};
}();