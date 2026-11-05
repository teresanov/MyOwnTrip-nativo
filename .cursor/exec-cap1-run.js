const parts = ['cap1_0', 'cap1_1', 'cap1_2', 'cap1_3', 'cap1_4', 'cap1_5'];
let code = '';
for (const k of parts) code += await figma.clientStorage.getAsync(k);
return await (0, eval)('(async () => {' + code + '})()');