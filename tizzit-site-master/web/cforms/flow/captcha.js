function passcode(length) {
  var alphabet = [ 'a', 'a', 'a', 'b', 'c', 'd', 'e', 'e', 'e', 'f', 'g', 'h', 'i',
                   'i', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'o', 'o', 'p', 'q', 'r',
                   's', 't', 'u', 'u', 'u', 'v', 'w', 'x', 'y', 'z']; 

  var result = "";
  for (var x = 0; x < length; x++) {
    var offset = Math.random() * alphabet.length;
    result += alphabet[Math.floor(offset)];
  }
  return result;
}