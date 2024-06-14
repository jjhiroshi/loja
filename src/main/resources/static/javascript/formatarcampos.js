const handleCPF = (event) => {
  let input = event.target;
  input.value = cpfMask(input.value);
}

const cpfMask = (value) => {
  if (!value)
   return "";

  value = value.replace(/(\d{3})(\d{3})(\d{3})(\d)/,"$1.$2.$3-$4");
  return value
}

const handlePhone = (event) => {
    let input = event.target;
    input.value = phoneMask(input.value);
  }
  
const phoneMask = (value) => {
if (!value)
 return "";

value = value.replace(/\D/g,'')
value = value.replace(/(\d{2})(\d)/,"($1) $2")
value = value.replace(/(\d)(\d{4})$/,"$1-$2")
return value
}

const handleCep = (event) => {
  let input = event.target;
  input.value = cepMask(input.value);
}

const cepMask = (value) => {
  if (!value)
    return "";

  value = value.replace(/(\d{5})(\d)/,"$1-$2")
  return value;
}
