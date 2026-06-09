
const MAIN='zrGAL4v6MEMc9hzZemU432';
if(figma.fileKey!==MAIN) throw new Error('ABORT');
const ICONS={"expand_more": "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"48\" height=\"48\" viewBox=\"0 -960 960 960\"><path d=\"M480-357.85 253.85-584l32.61-32.61L480-423.08l193.54-193.53L706.15-584 480-357.85Z\"/></svg>", "place": "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"48\" height=\"48\" viewBox=\"0 -960 960 960\"><path d=\"M140-140v-520h236.54v45.39H185.39v429.22h589.22v-429.22H583.46V-660H820v520H140Zm340-196.08L332.23-483.85l33-32.61L457.31-424v-506h45.38v506l92.46-92.46 32.62 32.61L480-336.08Z\"/></svg>"};
function applyFills(n,f){if(n.type==='VECTOR'&&f.length)n.fills=f;if('children'in n)for(const c of n.children)applyFills(c,f);}
function replace(comp,svg){const size=comp.width||24;const old=comp.children.slice();const oldIcon=comp.findOne(n=>n.name==='icon'||n.type==='VECTOR'||n.type==='FRAME');const fills=oldIcon?.fills?JSON.parse(JSON.stringify(oldIcon.fills)):[];const svgNode=figma.createNodeFromSvg(svg);const scale=Math.min(size/svgNode.width,size/svgNode.height);svgNode.rescale(scale);svgNode.x=(size-svgNode.width)/2;svgNode.y=(size-svgNode.height)/2;svgNode.name='icon';for(const c of old)c.remove();comp.appendChild(svgNode);applyFills(svgNode,fills);}
const section=await figma.getNodeByIdAsync('55594:2485');
const replaced=[],errors=[];
for(const comp of section.findAll(n=>n.type==='COMPONENT')){const svg=ICONS[comp.name];if(!svg)continue;try{replace(comp,svg);replaced.push(comp.name);}catch(e){errors.push({name:comp.name,err:String(e)});}}
return {part:15, replaced:replaced.length, names:[...new Set(replaced)], errors};
